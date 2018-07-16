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

    override fun run(): Any? {
        val start = System.currentTimeMillis()
        val ctx = RequestContext.getCurrentContext()
        val request = ctx.request
        val host = URI(request.requestURL.toString()).host
        // TODO 系统设置主站域名
        val hostArray = host.split(".").reversed()
        var result = Result.error<String>("未授权")
        val domainPrefix =
            if (hostArray.size >= 3 && hostArray[0] == "cn" && hostArray[1] == "dmka" && hostArray[2].startsWith("sip") && hostArray.size == 4) {
                hostArray[3]
            } else {
                "sip"
            }
        val uri = request.requestURI
        val services = RedisUtil.hGet<List<Service>>(Constant.Redis.APP, domainPrefix)
        services?.let {
            for (service in services) {
                if (antPathMatcher.match(service.path, uri)) {
                    val serviceUrl = "/" + antPathMatcher.extractPathWithinPattern(service.path, uri)
                    val authorization = request.getHeader(Constant.System.AUTHORIZATION)
                    if (authorization == null || authorization.length != 32) {
                        //未登录用户
                        val noLoginUser = RedisUtil.get<UserDto>(Constant.Redis.TOKEN_GUEST)
                        if (noLoginUser != null) {
                            noLoginUser.resources?.get(service.id)?.let { resources ->
                                if (resources.any { antPathMatcher.match(it, "/" + request.method + serviceUrl) }) {
                                    println(System.currentTimeMillis() - start)
                                    return null
                                }
                            }
                        }
                    } else {
                        val user = RedisUtil.get<UserDto>(Constant.Redis.TOKEN_PREFIX + authorization)
                        if (user == null) {
                            //auth存在但是redis不存在，可能是auth已过期或压根不存在返回为登录超时
                            result = Result.error("登录超时")
                        } else {
//                                auth存在并且redis存在无过期
                            user.resources?.get(service.id)?.let { resources ->
                                if (resources.any { antPathMatcher.match(it, "/" + request.method + serviceUrl) }) {
                                    println(System.currentTimeMillis() - start)
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
        }
//         这里依旧返回200文字提示未授权
        ctx.setSendZuulResponse(false)
        ctx.responseStatusCode = 200
        ctx.response.characterEncoding = "UTF-8"
        ctx.response.contentType = "application/json"
        ctx.responseBody = JSON.toJSONString(result)
        return null
    }
}
