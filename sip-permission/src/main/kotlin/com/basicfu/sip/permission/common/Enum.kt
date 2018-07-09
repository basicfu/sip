package com.basicfu.sip.permission.common

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
    enum class Menu constructor(val value: Int,val msg: String){
        EXIST_NAME(1,"菜单名已存在"),
        RESOURCE_NOT_FOUND(1,"部分要添加资源没有找到,请刷新最新菜单后重试")
    }
    enum class Role constructor(val value: Int,val msg: String){
        EXIST_NAME(1,"角色名已存在"),
        MENU_NOT_FOUND(1,"部分要添加菜单没有找到,请刷新最新菜单后重试")
    }
    enum class Resource constructor(val value: Int,val msg: String){
        EXIST_URL(1,"该服务下资源URL已存在"),
    }
    enum class Permission constructor(val value: Int,val msg: String){
        EXIST_NAME(1,"权限名已存在"),
        FIELD_EN_NAME_EXISTS(1,"字段英文名已存在"),
        SERVER(2,"服务端")
    }


}
