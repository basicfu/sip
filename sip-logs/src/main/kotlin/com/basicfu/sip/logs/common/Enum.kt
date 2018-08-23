package com.basicfu.sip.logs.common

/**
 * @author basicfu
 * @date 2018/8/23
 */
enum class Enum constructor(val value: Int,val msg:String) {
    ILLEGAL_REQUEST(6,"非法请求");
    enum class Template constructor(val value: Int,val msg:String) {
        EXISTS_NAME(201,"模板名已存在"),
        NOT_FOUND(201,"找不到要添加的模板"),
    }
}
