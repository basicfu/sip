package com.basicfu.sip.notify.common

/**
 * @author tianhu
 * @date 2018/7/18
 */
enum class Enum constructor(val value: Int, val msg:String){
    SERVER_ERROR(999,"服务器内部发生错误"),
    INVALID_MAIL_APPID(101,"无效的appId"),
    INVALID_MAIL_CODE(102,"无效的邮件模板code"),
    EMPTY_CONTENT(102,"邮件正文为空"),
    EMPTY_CONTENT_TEMPLATE(103,"未配置邮件模板正文"),
    ENABLE_TEMPLATE(104,"模板未启用"),
    EMPTY_SENDER(105,"未配置发件人"),
    MISSING_PARAMETER(3,"缺少必要的参数"),
    INVALID_PERMISSION(4,"无权操作"),
    LOGIN_TIMEOUT(5,"登录超时"),
    ILLEGAL_REQUEST(6,"非法请求");


    enum class SendType constructor(val value: String,val msg:String) {
        MASS("MASS","群发"),
        ALONE("ALONE", "单独发送")
    }
}