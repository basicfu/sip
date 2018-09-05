package com.basicfu.sip.getway.filter

import com.alibaba.fastjson.JSON
import com.basicfu.sip.core.common.Constant
import com.basicfu.sip.core.model.Result
import com.basicfu.sip.core.model.dto.UserDto
import com.basicfu.sip.core.model.po.Service
import com.basicfu.sip.core.util.RedisUtil
import org.springframework.http.HttpStatus
import org.springframework.util.AntPathMatcher
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

    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {
        val request = servletRequest as HttpServletRequest
        val response = servletResponse as HttpServletResponse
//        request.getRequestDispatcher("/123").forward(request,response)
        val uri = request.requestURI
        val pathArray = uri.split("/")
        var result = Result.error<String>("未授权")
        var domainPrefix = ""
        if (pathArray.size > 1) {
            domainPrefix = pathArray[1]
        }
        val appServices = RedisUtil.hGetAll<List<Service>>(Constant.Redis.APP)
        val services = arrayListOf<Service>()
        services.addAll(appServices[Constant.System.NAME] ?: arrayListOf())
        services.addAll(appServices[domainPrefix] ?: arrayListOf())
        for (service in services) {
            if (antPathMatcher.match(service.path, uri)) {
                val serviceUrl = "/" + antPathMatcher.extractPathWithinPattern(service.path, uri)
                val authorization = request.getHeader(Constant.System.AUTHORIZATION)
                if (authorization == null || authorization.length != 32) {
                    //未登录用户
                    val noLoginUser = RedisUtil.get<UserDto>(Constant.Redis.TOKEN_GUEST)
                    if (noLoginUser != null) {
                        noLoginUser.resources?.get(service.id.toString())?.let { resources ->
                            if (resources.any { antPathMatcher.match(it, "/" + request.method + serviceUrl) }) {
                                filterChain.doFilter(request, response)
                                return
                            }
                        }
                    }
                } else {
                    val user = RedisUtil.get<UserDto>(Constant.Redis.TOKEN_PREFIX + authorization)
                    if (user == null) {
                        //auth存在但是redis不存在，可能是auth已过期或压根不存在返回为登录超时（排除访客接口）
                        val noLoginUser = RedisUtil.get<UserDto>(Constant.Redis.TOKEN_GUEST)
                        if (noLoginUser != null) {
                            noLoginUser.resources?.get(service.id.toString())?.let { resources ->
                                if (resources.any { antPathMatcher.match(it, "/" + request.method + serviceUrl) }) {
                                    filterChain.doFilter(request, response)
                                    return
                                }
                            }
                        }
                        result = Result.error("登录超时")
                    } else {
                        //auth存在并且redis存在无过期
                        user.resources?.get(service.id.toString())?.let { resources ->
                            if (resources.any { antPathMatcher.match(it, "/" + request.method + serviceUrl) }) {
                                RedisUtil.expire(
                                    Constant.Redis.TOKEN_PREFIX + authorization,
                                    Constant.System.SESSION_TIMEOUT
                                )
                                filterChain.doFilter(request, response)
                                return
                            }
                        }
                    }
                }
                break
            }
        }
        response.status = HttpStatus.OK.value()
        response.characterEncoding = "UTF-8"
        response.contentType = "application/json"
        response.outputStream.write(JSON.toJSONString(result).toByteArray())
        response.outputStream.flush()
        response.outputStream.close()
    }

    override fun destroy() {}
}
