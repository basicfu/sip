package com.basicfu.sip.getway.filter

import com.alibaba.fastjson.JSON
import com.basicfu.sip.core.Constant
import com.basicfu.sip.core.model.Result
import com.basicfu.sip.core.model.dto.UserDto
import com.basicfu.sip.core.util.RedisUtil
import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext


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
        val ctx = RequestContext.getCurrentContext()
        val request = ctx.request
        var uri = request.requestURI
        /**
         * 处理uri，这里只处理了一层
         */
        uri = uri.slice(1 until uri.length)
        val index = uri.indexOf("/")
        uri = if (index == -1) {
            "/"
        } else {
            uri.slice(index until uri.length)
        }
        var result = Result<String>("未授权")
        val authorization = request.getHeader(Constant.System.AUTHORIZATION)
        val serviceId = "1"
        //first get redis
        val resourceId = RedisUtil.hGet<Long>(Constant.Redis.RESOURCE, "$serviceId|$uri")
        if (resourceId == null) {
            result = Result("不存在的资源")
        } else {
            if (authorization == null || authorization.length != 32) {
                //未登录用户
                val noLoginUser = RedisUtil.get<UserDto>(Constant.Redis.TOKEN_GUEST)
                if (noLoginUser?.resourceIds != null) {
                    if (noLoginUser.resourceIds!!.contains(resourceId)) {
                        return null
                    }
                }
            } else {
                val user = RedisUtil.get<UserDto>(Constant.Redis.TOKEN_PREFIX + authorization)
                if (user == null) {
                    //auth存在但是redis不存在，可能是auth已过期或压根不存在返回为登录超时
                    result = Result("登录超时")
                } else {
                    //auth存在并且redis存在无过期
                    if (user.resourceIds!=null&& user.resourceIds!!.contains(resourceId)) {
                        //暂时每次请求都刷新token过期时间
                        RedisUtil.expire(Constant.Redis.TOKEN_PREFIX + authorization, Constant.System.SESSION_TIMEOUT)
                        return null
                    }
                }
            }
        }
        // 这里依旧返回200文字提示未授权
        ctx.setSendZuulResponse(false)
        ctx.responseStatusCode = 200
        ctx.response.characterEncoding = "UTF-8"
        ctx.response.contentType = "application/json"
        ctx.responseBody = JSON.toJSONString(result)
        return null
    }
}
