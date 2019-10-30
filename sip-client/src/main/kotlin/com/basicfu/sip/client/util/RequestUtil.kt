package com.basicfu.sip.client.util

import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

object RequestUtil {

    fun getHeader(key: String): String? {
        val requestAttributes = RequestContextHolder.getRequestAttributes()
        if (requestAttributes != null) {
            val request = (requestAttributes as ServletRequestAttributes).request
            return request.getHeader(key)
        }
        return null
    }

    fun getParameter(key: String): String? {
        val requestAttributes = RequestContextHolder.getRequestAttributes()
        if (requestAttributes != null) {
            val request = (requestAttributes as ServletRequestAttributes).request
            return request.getParameter(key)
        }
        return null
    }
}
