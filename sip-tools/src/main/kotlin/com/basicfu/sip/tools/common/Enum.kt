package com.basicfu.sip.tools.common

/**
 * @author basicfu
 * @date 2018/8/16
 */
@Suppress("UNUSED_PARAMETER", "unused")
enum class Enum constructor(val value: Int, val msg:String) {
    SUCCESS(0,"成功"),
    ILLEGAL_REQUEST(6,"非法请求");

    enum class KubeCharts constructor(val value: Int,val msg: String){
        EXIST_NAME(1,"charts已存在")
    }
}
