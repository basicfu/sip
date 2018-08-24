package com.basicfu.sip.getway.filter

import com.alibaba.fastjson.JSON
import com.basicfu.sip.core.common.Constant
import com.basicfu.sip.core.model.Result
import com.basicfu.sip.core.model.dto.UserDto
import com.basicfu.sip.core.model.po.Service
import com.basicfu.sip.core.util.RedisUtil
import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext
import org.springframework.util.AntPathMatcher
import java.net.URI


/**
 * @author basicfu
 * @date 2018/7/12
 */

class PermissionFilter : ZuulFilter() {
    private val antPathMatcher = AntPathMatcher()

    override fun filterType(): String {
        return "pre"
    }

    override fun filterOrder(): Int {
        return 0
    }

    override fun shouldFilter(): Boolean {
        return true
    }

    @Suppress("UNCHECKED_CAST")
    override fun run(): Any? {
        val ctx = RequestContext.getCurrentContext()
        val request = ctx.request
        val uri = request.requestURI
        val pathArray=uri.split("/")
        var result = Result.error<String>("未授权")
        var domainPrefix=""
        if(pathArray.size>1){
            domainPrefix=pathArray[1]
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
                                return null
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
                                    return null
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
                                return null
                            }
                        }
                    }
                }
                break
            }
        }
        //这里依旧返回200文字提示未授权
        ctx.setSendZuulResponse(false)
        ctx.responseStatusCode = 200
        ctx.response.characterEncoding = "UTF-8"
        ctx.response.contentType = "application/json"
        ctx.responseBody = JSON.toJSONString(result)
        return null
    }
}
