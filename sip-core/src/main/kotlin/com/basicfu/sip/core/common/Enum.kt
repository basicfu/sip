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

    NOT_PERMISSION_ADD_SYSTEM_SUPER_ADMIN(1000, "无权操作系统超级管理员"),
    NOT_PERMISSION_ADD_SYSTEM_ADMIN(1001, "无权操作系统管理员"),
    NOT_PERMISSION_ADD_APP_SYSTEM_ADMIN(1002, "无权操作应用超级管理员"),
    NOT_PERMISSION_ADD_APP_ADMIN(1003, "无权操作应用管理员"),
    NOT_PERMISSION_ADD_APP_NORMAL(1004, "无权操作普通用户"),
    SUPER_ADMIN_NOT_CHANGE_USER_TYPE(1005, "超级管理员无法变更用户类型"),
    SYSTEM_USER_NOT_EXCHANGE_APP_USER(1006, "系统用户和应用用户不能变换"),

    REPEAT_REQUEST(101, "重复请求");

    enum class UserType constructor(val system: Boolean){
        SYSTEM_SUPER_ADMIN(true),
        SYSTEM_ADMIN(true),
        APP_SUPER_ADMIN(false),
        APP_ADMIN(false),
        NORMAL(false),
    }

    enum class LoginType constructor(val value: Int){
        USERNAME(0),
        MOBILE(1),
        EMAIL(2),
    }

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
