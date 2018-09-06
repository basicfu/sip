package com.basicfu.sip.core.common.wrapper

import com.basicfu.sip.core.common.Constant
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper

/**
 * @author basicfu
 * @date 2018/9/6
 * request包装类
 */
@Suppress("MemberVisibilityCanBePrivate")
class RequestWrapper(request: HttpServletRequest) : HttpServletRequestWrapper(request) {

    private val params = HashMap<String, Array<String>>()

    init {
        this.params.putAll(request.parameterMap)
    }

    /**
     * 如果有appId在后面追加appId
     * 由于zuul转发时使用getQueryString获取参数的方式转发，因此需要重写此方法而不是getParameter
     */
    override fun getQueryString(): String? {
        var queryString = super.getQueryString()
        if (queryString.isNullOrBlank()) {
            params[Constant.System.APP_ID]?.let { queryString = "${Constant.System.APP_ID}=${it[0]}" }
        } else {
            params[Constant.System.APP_ID]?.let { queryString += "&${Constant.System.APP_ID}=${it[0]}" }
        }
        return queryString
    }

    override fun getParameterNames(): Enumeration<String> {
        return Vector(params.keys).elements()
    }

    override fun getParameter(name: String): String? {
        val values = params[name]
        return if (values == null || values.isEmpty()) {
            null
        } else values[0]
    }

    override fun getParameterValues(name: String): Array<String>? {
        val values = params[name]
        return if (values == null || values.isEmpty()) {
            null
        } else values
    }

    @Suppress("UNCHECKED_CAST")
    fun addParameter(name: String, value: Any?) {
        if (value != null) {
            when (value) {
                is Array<*> -> params[name] = value as Array<String>
                is String -> params[name] = arrayOf(value)
                else -> params[name] = arrayOf(value.toString())
            }
        }
    }
}
