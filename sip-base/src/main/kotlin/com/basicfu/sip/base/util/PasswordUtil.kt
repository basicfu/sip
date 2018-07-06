package com.basicfu.sip.base.util

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

/**
 * @author basicfu
 * @date 2018/6/30
 */
object PasswordUtil {
    private val bcryptPasswordEncoder = BCryptPasswordEncoder()
    fun matches(password: String, encodedPassword: String): Boolean {
        return bcryptPasswordEncoder.matches(password, encodedPassword)
    }

    fun encode(password: String): String {
        return bcryptPasswordEncoder.encode(password)
    }
}
