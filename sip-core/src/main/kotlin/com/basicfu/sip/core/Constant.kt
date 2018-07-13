package com.basicfu.sip.core

/**
 * Constant
 *
 * @author basicfu
 * @date 2018/6/22
 */
object Constant {
    const val N='0'
    const val Y='1'

    object System {
        const val GUEST = "GUEST"
        const val AUTHORIZATION = "Authorization"
        const val SESSION_TIMEOUT: Long = 24 * 60 * 60 * 1000
    }

    object Redis {
        const val SERVICE = "SERVICE"
        const val TOKEN_PREFIX = "TOKEN_"
        const val TOKEN_GUEST = TOKEN_PREFIX + "_" + System.GUEST
        const val RESOURCE = "RESOURCE"
    }
}
