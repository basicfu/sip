package com.basicfu.sip.core.common

/**
 * 0正常
 * <0系统错误
 * >0业务错误
 * @author basicfu
 * @date 2018/3/25
 */
enum class Enum(val value: Int, val msg: String) {
    SUCCESS(0, "成功"),
    SERVER_ERROR(-1, "系统内部错误"),
    NOT_AVAILABLE(-2, "服务暂时不可用,请稍后重试"),

    NOT_LOGIN(1, "未登录"),
    UNAUTHORIZED(2, "未授权"),
    INVALID_PARAMETER(3, "无效的参数"),
    SECRET_INVALID(4, "SECRET无效"),
    NOT_FOUND_CALL_CODE(5, "没有找到被调应用CODE"),
    NOT_FOUND_APP_CODE(6, "没有找到应用CODE"),

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
