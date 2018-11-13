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
    REPEAT_REQUEST(7, "重复请求"),

    EXIST_APP_NAME(1000, "应用名已存在"),
    EXIST_APP_CODE(1001, "应用CODE已存在"),
    EXIST_APP_SERVICE_PATH(1002, "服务路径已存在"),
    EXIST_APP_SERVER_ID(1003, "服务ID已存在"),
    EXIST_APP_SERVER_URL(1004, "服务URL已存在"),
    NOT_NULL_SERVER_ID_OR_URL(1005, "注册名或URL必填一个"),
    EXIST_FIELD_NAME(1006, "字段名已存在"),
    EXIST_FIELD_EN_NAME(1007, "字段英文名已存在"),
    NOT_ADD_SYSTEM_FIELD(1008, "系统字段无法添加"),
    EXIST_USER(1009, "用户名已存在"),
    USERNAME_OR_PASSWORD_ERROR(1010, "用户名或密码错误"),
    THEN_USER_MAX_ONLINE(1011, "超过用户同时登录最大数"),
    LOGIN_ERROR(1012, "登录失败，请稍后重试"),
    NOT_PERMISSION_ADD_SYSTEM_SUPER_ADMIN(1013, "无权操作系统超级管理员"),
    NOT_PERMISSION_ADD_SYSTEM_ADMIN(1014, "无权操作系统管理员"),
    NOT_PERMISSION_ADD_APP_SYSTEM_ADMIN(1015, "无权操作应用超级管理员"),
    NOT_PERMISSION_ADD_APP_ADMIN(1016, "无权操作应用管理员"),
    NOT_PERMISSION_ADD_APP_NORMAL(1017, "无权操作普通用户"),
    SUPER_ADMIN_NOT_CHANGE_USER_TYPE(1018, "超级管理员无法变更用户类型"),
    SYSTEM_USER_NOT_EXCHANGE_APP_USER(1019, "系统用户和应用用户不能变换"),

    NOT_FOUND_DICT(2000, "找不到字典"),
    NO_DELETE_ROOT_DICT(2001, "不能删除根字典"),
    EXIST_DICT_VALUE(2002, "当前value已存在"),
    IMPORT_FORMAT_ERROR(2003, "导入格式错误"),
    NOT_NULL_DICT_NAME_AND_VALUE(2004, "字典名和字典Value不能为空"),

    NOT_FOUND_USER_ID(3000, "未找到用户ID"),
    EXIST_MENU_NAME(3001, "菜单名已存在"),
    NOT_FOUND_RESOURCE(3002, "部分要添加资源没有找到,请刷新数据后重试"),
    EXIST_ROLE_NAME(3003, "角色名已存在"),
    NOT_FOUND_MENU(3004, "部分要添加菜单没有找到,请刷新数据后重试"),
    NOT_FOUND_PERMISSION(3005, "部分要添加权限没有找到,请刷新数据后重试"),
    SERVICE_URL_METHOD_UNIQUE(3006, "一个服务下的资源URL和请求方法需要唯一"),
    EXIST_PERMISSION_NAME(3007, "权限名已存在"),
    EXIST_TEMPLATE_NAME(3008, "模板名已存在"),
    EXIST_PERMISSION_CODE(3009, "权限名CODE已存在"),
    NOT_FOUND_MENU_ID(3010, "找不到菜单ID"),
    DRAG_SORT_NEED_SAME_LEVEL(3011, "只能在同一层级拖动");

    enum class UserType constructor(val system: Boolean) {
        SYSTEM_SUPER_ADMIN(true),
        SYSTEM_ADMIN(true),
        APP_SUPER_ADMIN(false),
        APP_ADMIN(false),
        NORMAL(false),
    }

    enum class LoginType constructor(val value: Int) {
        USERNAME(0),
        MOBILE(1),
        EMAIL(2),
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
