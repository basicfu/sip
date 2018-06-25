package com.basicfu.sip.core.model

import com.alibaba.fastjson.JSONObject
import com.github.pagehelper.PageInfo
import java.util.*

class Result{
    var code = 0
    var success = true
    var rid = UUID.randomUUID().toString()
    var msg = ""
    var data: Any?=JSONObject()

    constructor() {}

    constructor(msg: String) {
        this.msg = msg
    }

    companion object {

        var insert = Result("添加成功")
        var update = Result("更新成功")
        var delete = Result("删除成功")
        var logout = Result("退出成功")

        fun success(data: PageInfo<*>): Result {
            val obj = JSONObject()
            obj["list"] = data.list
            val page = JSONObject()
            page["total"] = data.total
            page["pageSize"] = data.pageSize
            page["pageNum"] = data.pageNum
            obj["page"] = page
            val result = Result()
            result.data = obj
            return result
        }

        inline fun <reified T> success(data: List<T>): Result {
            val result = Result()
            result.data = data
            return result
        }

        fun success(data: Any): Result {
            val result = Result()
            result.data = data
            return result
        }

        fun success(data: Any, message: String): Result {
            val result = Result()
            result.data = data
            result.msg = message
            return result
        }

        fun success(msg: String): Result {
            val result = Result()
            result.msg = msg
            return result
        }

        fun error(code: Int?, msg: String): Result {
            val result = Result()
            result.code = code!!
            result.success = false
            result.msg = msg
            return result
        }

        fun error(code: Int?, msg: String, data: Any): Result {
            val result = Result()
            result.code = code!!
            result.success = false
            result.msg = msg
            result.data = data
            return result
        }
    }
}
