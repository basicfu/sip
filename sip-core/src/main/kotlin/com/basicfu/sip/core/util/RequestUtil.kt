package com.basicfu.sip.core.util

import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

object RequestUtil {


    fun getHeader(name: String): String? {
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        return request.getHeader(name)
    }
}
