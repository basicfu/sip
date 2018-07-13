package com.basicfu.sip.getway.filter

import com.alibaba.fastjson.JSON
import com.basicfu.sip.core.Constant
import com.basicfu.sip.core.model.Result
import com.basicfu.sip.core.model.dto.UserDto
import com.basicfu.sip.core.util.RedisUtil
import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext
import java.net.URI


/**
 * @author basicfu
 * @date 2018/7/12
 */

class PermissionFilter : ZuulFilter() {

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
        return null

//        val ctx = RequestContext.getCurrentContext()
//        val request = ctx.request
//        val host = URI(request.requestURL.toString()).host
//        // TODO 系统设置主站域名
//        val hostArray = host.split(".").reversed()
//        var result = Result.error<String>("未授权")
//        if (hostArray.size >= 3 && hostArray[0] == "cn" && hostArray[1] == "dmka" && hostArray[2].startsWith("sip")) {
//            val domainPrefix = if (hostArray.size == 4) {
//                hostArray[3]
//            } else {
//                "sip"
//            }
////            val all = RedisUtil.hGet<ApplicationDto>(Constant.Redis.SERVICE,)
////            all.forEach { _, v ->
////                if (v != null) {
////                    val zuulRoute = ZuulProperties.ZuulRoute()
////                    zuulRoute.id = v.id.toString()
////                    zuulRoute.path = v.path
////                    zuulRoute.serviceId = v.serverId
////                    zuulRoute.url = v.url
////                    zuulRoute.isStripPrefix = v.stripPrefix!!
////                    zuulRoute.retryable = v.retryable
////                    routesMap[zuulRoute.path] = zuulRoute
////                }
////            }
//            var uri = request.requestURI
////            /base/user/get/1/hello
////            /base/user/get/{id}/hello  /aa/**/bb
////            /base/{id}/zz/
//            uri = uri.slice(1 until uri.length)
//            val index = uri.indexOf("/")
//            uri = if (index == -1) {
//                "/"
//            } else {
//                uri.slice(index until uri.length)
//            }
//            val authorization = request.getHeader(Constant.System.AUTHORIZATION)
//            val serviceId = "1"
//            //first get redis
//            val resourceId = RedisUtil.hGet<Long>(Constant.Redis.RESOURCE, "$serviceId|$uri")
//            if (resourceId == null) {
//                result = Result.error("不存在的资源")
//            } else {
//                if (authorization == null || authorization.length != 32) {
//                    //未登录用户
//                    val noLoginUser = RedisUtil.get<UserDto>(Constant.Redis.TOKEN_GUEST)
//                    if (noLoginUser?.resourceIds != null) {
//                        if (noLoginUser.resourceIds!!.contains(resourceId)) {
//                            return null
//                        }
//                    }
//                } else {
//                    val user = RedisUtil.get<UserDto>(Constant.Redis.TOKEN_PREFIX + authorization)
//                    if (user == null) {
//                        //auth存在但是redis不存在，可能是auth已过期或压根不存在返回为登录超时
//                        result = Result.error("登录超时")
//                    } else {
//                        //auth存在并且redis存在无过期
//                        if (user.resourceIds != null && user.resourceIds!!.contains(resourceId)) {
//                            //暂时每次请求都刷新token过期时间
//                            RedisUtil.expire(
//                                Constant.Redis.TOKEN_PREFIX + authorization,
//                                Constant.System.SESSION_TIMEOUT
//                            )
//                            return null
//                        }
//                    }
//                }
//            }
//        }
        // 这里依旧返回200文字提示未授权
//        ctx.setSendZuulResponse(false)
//        ctx.responseStatusCode = 200
//        ctx.response.characterEncoding = "UTF-8"
//        ctx.response.contentType = "application/json"
//        ctx.responseBody = JSON.toJSONString(result)
        return null
    }
}
