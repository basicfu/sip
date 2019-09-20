package com.basicfu.sip.base.service

import com.basicfu.sip.base.common.constant.Constant
import com.basicfu.sip.base.model.biz.RoleToken
import com.basicfu.sip.base.util.TokenUtil
import com.basicfu.sip.core.common.exception.CustomException
import com.basicfu.sip.core.model.Result
import com.basicfu.sip.core.util.RedisUtil
import com.basicfu.sip.core.util.RequestUtil
import org.springframework.stereotype.Service
import org.springframework.util.AntPathMatcher

@Service
class SipService {
    private val antPathMatcher = AntPathMatcher()

    fun permissionCheck(map:HashMap<String,String>): Result<Any> {
        val app = RequestUtil.getHeader(Constant.System.APP) ?: throw CustomException("找不到应用信息")
        val authorization = RequestUtil.getHeader(Constant.System.AUTHORIZATION)
        val roles = arrayListOf(Constant.System.GUEST)
        val targetUrl = "/${map["method"]}${map["url"]}"
        if (authorization.isNullOrBlank()) {
            //不带token未授权
            if (!allowRequest(roles, app, targetUrl)) {
                throw CustomException("未授权")
            } else {
                return Result.success(null, null)
            }
        }
        val user = TokenUtil.getCurrentUser(authorization)
        return if (user == null) {
            //带token又未查到用户信息，登录过期，但是还会处理不需要权限的接口
            if (!allowRequest(roles, app, targetUrl)) {
                throw CustomException("登录过期")
            } else {
                Result.success<Any>(null, null)
            }
        } else {
            //带token登录信息又未过期，未授权
            if (!allowRequest(roles, app, targetUrl)) {
                throw CustomException("未授权")
            } else {
                Result.success<Any>(null, null)
            }
        }
    }

    private fun allowRequest(roles: List<String>, app: String, url: String): Boolean {
        val resources = arrayListOf<String>()
        val roleTokenMap = RedisUtil.hGetAll<RoleToken>(Constant.Redis.ROLE_PERMISSION)
        val values = roleTokenMap.filter { roles.contains(it.key) }.values
        values.forEach { roleToken ->
            roleToken!!.resources[app]?.let { resources.addAll(it) }
        }
        if (resources.any { antPathMatcher.match(it, url) }) {
            return true
        }
        return false
    }
}
