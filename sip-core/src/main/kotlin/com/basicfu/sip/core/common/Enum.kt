package com.basicfu.sip.core.common

/**
 * Enum
 *
 * @author basicfu
 * @date 2018/3/25
 */
enum class Enum private constructor(val value: Int, val msg: String) {
    SUCCESS(0, "成功"),

    SERVER_ERROR(1, "服务器内部发生错误,请稍后重试"),
    INVALID_PARAMETER(1, "无效的参数"),
    DATA_NOT_EXIST(5, "数据不存在"),
    REPEAT_REQUEST(101, "重复请求");


    enum class Status private constructor(val value: String) {
        SUCCESS("SUCCESS"),
        BIZ_ERROR("BIZ_ERROR"),
        SYS_ERROR("SYS_ERROR")
    }

    enum class RefundType private constructor(val value: String) {
        SEND_BACK("SEND_BACK"),
        VISIT("VISIT")
    }
}
