package com.basicfu.sip.core.util

import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import javax.servlet.http.HttpServletRequest

object RequestUtil {

    fun getRequest(): HttpServletRequest {
        return (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
    }

    fun getHeader(key: String): String? {
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        return request.getHeader(key)
    }

    fun getParameter(key: String): String? {
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        return request.getParameter(key)
    }
}
