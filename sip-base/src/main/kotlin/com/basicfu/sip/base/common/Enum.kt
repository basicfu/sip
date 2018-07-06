package com.basicfu.sip.base.common

/**
 * @author fuliang
 * @date 17/10/26
 */
@Suppress("UNUSED_PARAMETER", "unused")
enum class Enum constructor(val value: Int, val msg:String) {
    SUCCESS(0,"成功"),
    SERVER_ERROR(1,"服务器内部发生错误"),
    INVALID_PARAMETER(2,"无效的参数"),
    MISSING_PARAMETER(3,"缺少必要的参数"),
    INVALID_PERMISSION(4,"无权操作"),
    LOGIN_TIMEOUT(5,"登录超时"),
    ILLEGAL_REQUEST(6,"非法请求");

    enum class Dict{
        TEXT
    }

    enum class UserTemplate constructor(val value: Int,val msg: String){
        FIELD_NAME_EXISTS(1,"字段名已存在"),
        FIELD_EN_NAME_EXISTS(1,"字段英文名已存在"),
        SERVER(2,"服务端")
    }
    enum class User constructor(val value: Int,val msg: String){
        EXIST_USER(1,"用户名已存在"),
        SERVER(2,"服务端")
    }
    enum class FieldType{
        TEXT,
        NUMBER,
        CHECK,
        RADIO,
        SELECT,
        DATE
    }


}
