package com.basicfu.sip.core.util

import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.*

object TokenUtil {

    fun generateToken(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }

    /**
     * 获取当前用户的token
     */
    fun getToken(): String {
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        return request.getHeader("token")
    }

    /**
     * 获取当前用户的user对象
     */
    fun getUser() {
//        val token = getToken()
        //
    }
}
