package com.basicfu.sip.core.util

import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import sun.misc.BASE64Encoder
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

object TokenUtil {

    fun generateToken():String{
        val sdf = SimpleDateFormat("yyyyMMddHHmmss")
        val str = Math.random().toString() + sdf.format(Calendar.getInstance().time) + UUID.randomUUID().toString()
        val md5 = MessageDigest.getInstance("MD5")
        val base64en = BASE64Encoder()
        return base64en.encode(md5.digest(str.toByteArray()))
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
        val token = getToken()
        //
    }
}
