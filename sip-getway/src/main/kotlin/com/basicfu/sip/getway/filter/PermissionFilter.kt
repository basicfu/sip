package com.basicfu.sip.getway.filter

import com.alibaba.fastjson.JSON
import com.basicfu.sip.core.common.Constant
import com.basicfu.sip.core.common.wrapper.RequestWrapper
import com.basicfu.sip.core.model.Result
import com.basicfu.sip.core.model.dto.AppDto
import com.basicfu.sip.core.model.dto.AppServiceDto
import com.basicfu.sip.core.model.dto.UserDto
import com.basicfu.sip.core.util.RedisUtil
import com.netflix.zuul.context.RequestContext
import org.apache.commons.lang3.StringUtils
import org.springframework.http.HttpStatus
import org.springframework.util.AntPathMatcher
import java.net.URI
import java.util.*
import javax.servlet.*
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper
import javax.servlet.http.HttpServletResponse


/**
 * @author basicfu
 * @date 2018/7/12
 */
@WebFilter(filterName = "permissionFilter", urlPatterns = ["/*"])
class PermissionFilter : Filter {
    private val antPathMatcher = AntPathMatcher()

    override fun init(filterConfig: FilterConfig) {
    }

    /**
     * 区分应用方式按优先级:
     * 1.域名(生产环境建议使用内网)
     * 2.请求头app
     * 3.请求参数app
     * 4.第一级path
     *
     * 区分需要被调用的应用，默认为当前应用
     * 1.请求头call
     * 2.请求参数call
     *
     * 授权方式:
     * 如果带有secret参数且验证通过可以正常请求忽略token验证,如果没有secret将使用token方式验证
     * 1.secret:
     * 1.1.请求头secret
     * 1.2.请求参数secret
     *
     * 2.token
     * 2.1.请求头Authorization
     */
    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {
        val request = RequestWrapper(servletRequest as HttpServletRequest)
        val response = servletResponse as HttpServletResponse
        var uri = request.requestURI
        val pathArray = uri.split("/")
        var appCode: String? = null
        var appCall: String? = null
        var appSecret: String? = null
        var pathFull = false
        //1.get domain code,eg:*.sip
        val host = URI(request.requestURL.toString()).host
        val hostArray = host.split(".")
        if (hostArray.size == 2 && hostArray[1] == Constant.System.APP_SYSTEM_CODE) {
            appCode = hostArray[0]
        }
        //2.get request header app code
        if (appCode == null) {
            appCode = request.getHeader(Constant.System.APP_CODE)
        }
        //3.get request parameter app code
        if (appCode == null) {
            appCode = request.getParameter(Constant.System.APP_CODE)
        }
        //4.get path code
        if (appCode == null && pathArray.size >= 3) {
            appCode = pathArray[1]
            uri=uri.substringAfter(appCode)
            pathFull = true
        }
        if (appCode == null) {
            returnMsg(response, "not found app code")
            return
        }
        val apps = RedisUtil.hGetAll<AppDto>(Constant.Redis.APP)
        val app = apps[appCode]
        if (app == null) {
            returnMsg(response, "not found app code")
            return
        }
        //1.get request header app call
        if (appCall == null) {
            appCall = request.getHeader(Constant.System.APP_CALL)
        }
        //2.get request parameter app call
        if (appCall == null) {
            appCall = request.getParameter(Constant.System.APP_CALL)
        }
        if(appCall!=null&&apps[appCall]==null){
            returnMsg(response, "not found call code")
            return
        }
        //1.get request header app secret
        if (appSecret == null) {
            appSecret = request.getHeader(Constant.System.APP_SECRET)
        }
        //2.get request parameter app secret
        if (appSecret == null) {
            appSecret = request.getParameter(Constant.System.APP_SECRET)
        }
        //set current thread app code
        request.addParameter(Constant.System.APP_ID,app.id)
        //每个应用只能调用sip中的服务和自身应用的服务并配置权限
        val services = arrayListOf<AppServiceDto>()
        services.addAll(apps[Constant.System.APP_SYSTEM_CODE]?.services ?: arrayListOf())
        services.addAll(apps[appCode]?.services ?: arrayListOf())
        var allow = false
        for (service in services) {
            //多个service的path一致也不影响匹配
            if (antPathMatcher.match(service.path, uri)) {
                //secret
                if (appSecret != null) {
                    if (app.secrets?.map { it.secret }?.contains(appSecret) == true) {
                        //correct secret ignore permission filter
                        allow = true
                        break
                    } else {
                        returnMsg(response, "secret invalid")
                        return
                    }
                }
                //token
                val serviceUrl = "/" + antPathMatcher.extractPathWithinPattern(service.path, uri)
                val authorization = request.getHeader(Constant.System.AUTHORIZATION)
                if (authorization == null || authorization.length != 32) {
                    //未登录用户
                    val noLoginUser = RedisUtil.get<UserDto>(Constant.Redis.TOKEN_GUEST)
                    if (noLoginUser != null) {
                        val resources = noLoginUser.resources?.get(service.id.toString())
                        if (resources?.any { antPathMatcher.match(it, "/" + request.method + serviceUrl) } == true) {
                            allow = true
                            break
                        }
                    }
                } else {
                    val user = RedisUtil.get<UserDto>(Constant.Redis.TOKEN_PREFIX + authorization)
                    if (user == null) {
                        //auth存在但是redis不存在，可能是auth已过期或压根不存在返回为登录超时（排除访客接口）
                        val noLoginUser = RedisUtil.get<UserDto>(Constant.Redis.TOKEN_GUEST)
                        if (noLoginUser != null) {
                            val resources = noLoginUser.resources?.get(service.id.toString())
                            if (resources?.any {
                                    antPathMatcher.match(it, "/" + request.method + serviceUrl)
                                } == true) {
                                allow = true
                                break
                            }
                        }
                        returnMsg(response, "login timeout")
                    } else {
                        //auth存在并且redis存在无过期
                        val resources = user.resources?.get(service.id.toString())
                        if (resources?.any { antPathMatcher.match(it, "/" + request.method + serviceUrl) } == true) {
                            RedisUtil.expire(
                                Constant.Redis.TOKEN_PREFIX + authorization,
                                Constant.System.SESSION_TIMEOUT
                            )
                            allow = true
                            break
                        }
                    }
                }
                break
            }
        }
        //must permission pass allow forward
        if (allow) {
            if (!pathFull) {
                uri = if(appCall!=null){
                    "/$appCall$uri"
                }else{
                    "/$appCode$uri"
                }
                request.getRequestDispatcher(uri).forward(request, response)
            }else{
                if(appCall!=null){
                    uri="$appCall${uri.substringAfter(appCode)}"
                    request.getRequestDispatcher(uri).forward(request, response)
                }else{
                    filterChain.doFilter(request, response)
                }
            }
        } else {
            returnMsg(response, "no auth")
        }
    }

    override fun destroy() {}

    private fun returnMsg(response: HttpServletResponse, msg: String) {
        val result = Result.error<String>(msg)
        response.status = HttpStatus.OK.value()
        response.characterEncoding = "UTF-8"
        response.contentType = "application/json"
        response.outputStream.write(JSON.toJSONString(result).toByteArray())
        response.outputStream.flush()
        response.outputStream.close()
    }
}
