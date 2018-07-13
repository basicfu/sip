package com.basicfu.sip.core.model

import com.alibaba.fastjson.JSONObject
import com.github.pagehelper.PageInfo
import java.util.*

@Suppress("UNCHECKED_CAST")
class Result<T> {
    var code = 0
    var msg = ""
    var success: Boolean = true
    var rid = UUID.randomUUID().toString()
    var data: T? = null

    companion object {
        inline fun <reified T> success(data: T?, msg: String?=null): Result<T> {
            val result = Result<T>()
            when (data) {
                is Int -> result.msg = "操作成功" + data.toString() + "条数据"
                is List<*> -> result.data = data
                is PageInfo<*> -> {
                    val obj = JSONObject()
                    obj["list"] = data.list
                    val page = JSONObject()
                    page["total"] = data.total
                    page["pageSize"] = data.pageSize
                    page["pageNum"] = data.pageNum
                    obj["page"] = page
                    result.data = obj as T
                }
                else -> result.data = data
            }
            if (msg != null) {
                result.msg = msg
            }
            return result
        }

        inline fun <reified T> error(msg: String = "", code: Int = 0, data: T? = null): Result<T> {
            val result = Result<T>()
            result.msg = msg
            result.code = code
            result.data = data
            result.success = false
            return result
        }
    }
}
