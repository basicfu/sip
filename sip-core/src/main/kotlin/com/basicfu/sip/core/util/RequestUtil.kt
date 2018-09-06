package com.basicfu.sip.core.util

import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

object RequestUtil {

    fun getHeader(key: String): String? {
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        return request.getHeader(key)
    }

    fun getParameter(key: String): String? {
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        return request.getParameter(key)
    }

    fun setParameter(key: String,value:String){
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        request.setAttribute(key,value)
    }
}
