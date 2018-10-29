package com.basicfu.sip.base.common

/**
 * @author fuliang
 * @date 17/10/26
 */
@Suppress("UNUSED_PARAMETER", "unused")
enum class Enum constructor(val value: Int, val msg: String) {
    SUCCESS(0, "成功"),
    SERVER_ERROR(1, "服务器内部发生错误"),
    INVALID_PARAMETER(2, "无效的参数"),
    MISSING_PARAMETER(3, "缺少必要的参数"),
    INVALID_PERMISSION(4, "无权操作"),
    LOGIN_TIMEOUT(5, "登录超时"),
    ILLEGAL_REQUEST(6, "非法请求");

    enum class Dict {
        TEXT
    }

    enum class App constructor(val value: Int, val msg: String) {
        NAME_EXISTS(1, "应用名已存在"),
        CODE_EXISTS(2, "应用CODE已存在")
    }

    enum class Service constructor(val value: Int, val msg: String) {
        PATH_EXISTS(1, "服务路径已存在"),
        SERVER_ID_EXISTS(1, "服务ID已存在"),
        URL_EXISTS(1, "服务URL已存在"),
        SERVER_ID_OR_URL(1, "注册名或URL必填一个"),
    }

    enum class UserTemplate constructor(val value: Int, val msg: String) {
        NAME_EXISTS(1, "字段名已存在"),
        EN_NAME_EXISTS(1, "字段英文名已存在"),
        SYSTEM_FIELD(2, "系统字段无法添加")
    }

    enum class User constructor(val value: Int, val msg: String) {
        EXIST_USER(1000, "用户名已存在"),
        USERNAME_OR_PASSWORD_ERROR(1000, "用户名或密码错误"),
        THEN_USER_MAX_ONLINE(1000, "超过用户同时登录最大数"),
        LOGIN_ERROR(1000, "登录失败，请稍后重试")
    }

    enum class FieldType {
        TEXT,
        NUMBER,
        CHECK,
        RADIO,
        SELECT,
        DATE
    }

    enum class Condition {
        IS_NULL,
        IS_NOT_NULL,
        EQUAL_TO,
        NOT_EQUAL_TO,
        GREATER_THEN,
        GREATER_THEN_OR_EQUAL_TO,
        LESS_THEN,
        LESS_THEN_OR_EQUAL_TO,
        IN,
        NOT_IN,
        BETWEEN,
        NOT_BETWEEN,
        LIKE,
        NOT_LIKE,
    }

}
