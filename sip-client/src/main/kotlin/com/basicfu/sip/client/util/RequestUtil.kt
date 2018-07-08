package com.basicfu.sip.client.util

import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

object RequestUtil {

    /**
     * 获取当前用户的token
     */
    fun getToken(): String? {
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        return request.getHeader("token")
    }
}
