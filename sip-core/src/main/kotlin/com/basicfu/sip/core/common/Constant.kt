package com.basicfu.sip.core.common

/**
 * Constant
 *
 * @author basicfu
 * @date 2018/6/22
 */
object Constant {
    object System {
        const val GUEST = "GUEST"
        const val AUTHORIZATION = "Authorization"
        const val SESSION_TIMEOUT: Long = 24 * 60 * 60 * 1000
        const val LOGOUT = "注销成功"
        const val PAGE_SIZE = 20
        const val PAGE_SIZE_STR = PAGE_SIZE.toString()
        const val APP_SYSTEM_CODE = "sip"
        const val APP_CODE = "app"
        const val APP_CALL = "call"
        const val APP_SECRET = "secret"
        const val APP_SKIP = "skip"
    }

    object Redis {
        const val TOKEN_PREFIX = "TOKEN_"
        const val TOKEN_GUEST = TOKEN_PREFIX + System.GUEST + "_"
        const val APP = "APP"
        const val DICT = "DICT_"
    }

    object Dict {
        const val USER_TEMPLATE_FIELD_TYPE = "USER_TEMPLATE_FIELD_TYPE"
    }
}
