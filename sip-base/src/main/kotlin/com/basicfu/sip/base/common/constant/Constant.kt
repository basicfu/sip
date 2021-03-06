package com.basicfu.sip.base.common.constant

/**
 * Constant
 *
 * @author basicfu
 * @date 2018/6/22
 */
object Constant {
    object System {
        const val APP = "app"
        const val NORMAL = "NORMAL"
        private const val PAGE_SIZE = 20
        const val GUEST = "GUEST"
        const val AUTHORIZATION = "Authorization"
        const val SESSION_TIMEOUT: Long = 24 * 60 * 60 * 1000
        const val LOGOUT = "注销成功"
        const val PAGE_SIZE_STR = PAGE_SIZE.toString()
        const val APP_SYSTEM_CODE = "sip"
        const val APP_ID = "_appId"
        const val APP_CODE = "_appCode"
        const val GETWAY_APP_CODE = "app"
        const val GETWAY_APP_CALL = "call"
        const val GETWAY_APP_SECRET = "secret"
        const val NOT_CHECK_APP = "notCheckApp"
        const val AES_TOKEN_KEY = "eGZdsCs0GQzE49SE"
    }

    object Redis {
        const val TOKEN_PREFIX = "TOKEN_"
        const val TOKEN_FORCED_PREFIX = "TOKEN_FORCED_"
        const val TOKEN_GUEST = TOKEN_PREFIX + System.GUEST + "_"
        const val APP = "APP"
        const val ROLE_PERMISSION = "ROLE_PERMISSION"
        const val SMS_CHECK = "SMS_"
    }

    object Dict {
        const val USER_TEMPLATE_FIELD_TYPE = "USER_TEMPLATE_FIELD_TYPE"
    }
}
