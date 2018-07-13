package com.basicfu.sip.dict.common

/**
 * @author fuliang
 * @date 17/10/26
 */
enum class Enum constructor(val value: Int,val msg:String) {
    SUCCESS(0,"成功"),
    SERVER_ERROR(1,"服务器内部发生错误"),
    INVALID_PARAMETER(2,"无效的参数"),
    MISSING_PARAMETER(3,"缺少必要的参数"),
    INVALID_PERMISSION(4,"无权操作"),
    LOGIN_TIMEOUT(5,"登录超时"),
    ILLEGAL_REQUEST(6,"非法请求");
    enum class Dict constructor(val value: Int,val msg:String) {
        NOT_FOUND(201,"找不到字典"),
        ID_PID_REPEAT(202,"父类型不能是当前类型"),
        NO_DELETE_ROOT(202,"不能删除根字典"),
        VALUE_REPEAT(202,"当前value已存在"),
        IMPORT_FORMAT_ERROR(202,"导入格式错误"),
        NAME_AND_VALUE_NOT_FOUND(202,"字典名和字典Value不能为空"),
    }

}
