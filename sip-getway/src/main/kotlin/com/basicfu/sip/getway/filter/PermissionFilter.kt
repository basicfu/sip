package com.basicfu.sip.getway.filter

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.basicfu.sip.common.constant.Constant
import com.basicfu.sip.common.enum.Enum
import com.basicfu.sip.common.model.Result
import com.basicfu.sip.common.model.dto.AppDto
import com.basicfu.sip.common.model.dto.AppServiceDto
import com.basicfu.sip.common.model.dto.UserDto
import com.basicfu.sip.common.model.redis.RoleToken
import com.basicfu.sip.common.util.AppUtil
import com.basicfu.sip.common.util.TokenUtil
import com.basicfu.sip.core.common.wrapper.RequestWrapper
import com.basicfu.sip.core.util.RedisUtil
import org.springframework.http.HttpStatus
import org.springframework.util.AntPathMatcher
import java.net.URI
import javax.servlet.*
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest
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
        // TODO 以后决定是否需要判断某些用户是否能执行 只有系统管理员能查看各个应用  app=sip  app=wutong&call=sip
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
            uri = uri.substringAfter(appCode)
            pathFull = true
        }
        if (appCode == null) {
            returnMsg(response, Enum.NOT_FOUND_APP_CODE)
            return
        }
        val apps = RedisUtil.hGetAll<AppDto>(Constant.Redis.APP)
        val app = apps[appCode]
        if (app == null) {
            returnMsg(response, Enum.NOT_FOUND_APP_CODE)
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
        if (appCall != null && apps[appCall] == null) {
            returnMsg(response, Enum.NOT_FOUND_CALL_CODE)
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
        val appId = app.id!!
        //set current thread app info,overwrite app parameter
        val appInfo = JSONObject()
        appInfo[Constant.System.APP_ID] = appId
        appInfo[Constant.System.APP_CODE] = appCode
        request.addParameter(Constant.System.APP_CODE, appInfo.toJSONString())
        var allow = false
        //有限判断用户token信息
        val frontToken = request.getHeader(Constant.System.AUTHORIZATION)
        var user: UserDto? = null
        if (frontToken != null) {
            user = TokenUtil.getCurrentUser()
        }
        //针对user中type为系统超级管理员跳过权限
        if (user != null && user.type == Enum.UserType.SYSTEM_SUPER_ADMIN.name) {
            allow = true
        } else {
            //每个应用只能调用sip中的服务和自身应用的服务并配置权限
            val services = arrayListOf<AppServiceDto>()
            services.addAll(apps[Constant.System.APP_SYSTEM_CODE]?.services ?: arrayListOf())
            if (Constant.System.APP_SYSTEM_CODE != appCode) {
                services.addAll(apps[appCode]?.services ?: arrayListOf())
            }
            val roleCodes=arrayListOf(Constant.System.GUEST)
            user?.roles?.let { roleCodes.addAll(it) }
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
                            returnMsg(response, Enum.SECRET_INVALID)
                            return
                        }
                    }
                    //token
                    val serviceUrl = "/${request.method}/${antPathMatcher.extractPathWithinPattern(service.path, uri)}"
                    if (frontToken == null) {
                        //未登录用户（排除访客接口）
                        if (allowRequest(false,roleCodes,appCode, service.id!!, serviceUrl)) {
                            allow = true
                            break
                        }
                        returnMsg(response, Enum.NOT_LOGIN)
                    } else {
                        if (user == null) {
                            //auth存在但是redis不存在，可能是auth已过期或压根不存在返回为登录超时（排除访客接口）
                            if (allowRequest(false,roleCodes,appCode, service.id!!, serviceUrl)) {
                                allow = true
                                break
                            }
                            returnMsg(response, Enum.NOT_LOGIN)
                        } else {
                            //auth存在并且redis存在无过期
                            if (allowRequest(true,roleCodes,appCode, service.id!!, serviceUrl)) {
                                allow = true
                                break
                            }
                        }
                    }
                    break
                }
            }
        }
        //must permission pass allow forward
        if (allow) {
            if (!pathFull) {
                uri = if (appCall != null) {
                    "/$appCall$uri"
                } else {
                    "/$appCode$uri"
                }
                request.getRequestDispatcher(uri).forward(request, response)
            } else {
                if (appCall != null) {
                    uri = "$appCall${uri.substringAfter(appCode)}"
                    request.getRequestDispatcher(uri).forward(request, response)
                } else {
                    filterChain.doFilter(request, response)
                }
            }
        } else {
            returnMsg(response, Enum.UNAUTHORIZED)
        }
    }

    override fun destroy() {}

    private fun allowRequest(login:Boolean,roleCodes:List<String>,appCode: String, serviceId: Long, url: String): Boolean {
        val appRoleToken = RedisUtil.hGetAll<RoleToken>("${Constant.Redis.ROLE_PERMISSION}$appCode")
        val values = appRoleToken.filter { roleCodes.contains(it.key) }.values
        val resources= arrayListOf<String>()
        values.forEach {roleToken->
            roleToken!!.resources!![serviceId]?.let { resources.addAll(it) }
        }
        if (resources.any { antPathMatcher.match(it, url) }) {
            if(login){
                RedisUtil.expire(TokenUtil.getCurrentToken()!!,Constant.System.SESSION_TIMEOUT)
            }
            return true
        }
        return false
    }

    private fun returnMsg(response: HttpServletResponse, msg: Enum) {
        val result = Result.error<String>(msg.msg, msg.value)
        response.status = HttpStatus.OK.value()
        response.characterEncoding = "UTF-8"
        response.contentType = "application/json"
        response.outputStream.write(JSON.toJSONString(result).toByteArray())
        response.outputStream.flush()
        response.outputStream.close()
    }
}
