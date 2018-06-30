package com.basicfu.sip.client.model

import java.util.*

@Suppress("UNCHECKED_CAST")
class Result<T> {
    var code = 0
    var msg = ""
    var success: Boolean = true
    var rid = UUID.randomUUID().toString()
    var data: T? = null

    /**
     * 字符串类型开始且泛型结尾为错误结果
     */
    @JvmOverloads
    constructor(msg: String = "", code: Int = 0, data: T? = null) {
        this.msg = msg
        this.code = code
        this.data = data
        this.success = false
    }

    /**
     * 第一个类型为泛型开始为正确结果
     */
    @JvmOverloads
    constructor(data: T?, msg: String? = null) {
        when (data) {
            is Int -> this.msg = "操作成功" + data.toString() + "条数据"
            is List<*> -> this.data = data
        }
        if (msg != null) {
            this.msg = msg
        }
    }

}
