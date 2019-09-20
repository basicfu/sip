package com.basicfu.sip.client.filter

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.basicfu.sip.client.util.ApiUtil
import org.springframework.http.HttpStatus
import javax.servlet.*
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * 内置权限过滤器
 * 可配置是否开启内置过滤器，或自己实现
 */
@WebFilter(filterName = "permissionFilter", urlPatterns = ["/user"])
class PermissionFilter : Filter {
    override fun init(filterConfig: FilterConfig) {
    }

    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {
        val request = servletRequest as HttpServletRequest
        val response = servletResponse as HttpServletResponse
        val json = ApiUtil.checkPermission(request.method, request.requestURI)
        if (json.getBoolean("success")) {
            filterChain.doFilter(request, servletResponse)
        } else {
            val result = JSONObject()
            result["success"] = false
            result["msg"] = json.getString("msg")
            returnMsg(response, JSON.toJSONString(result))
        }
    }

    override fun destroy() {}
    private fun returnMsg(response: HttpServletResponse, result: String) {
        response.status = HttpStatus.OK.value()
        response.characterEncoding = "UTF-8"
        response.contentType = "application/json"
        response.outputStream.write(result.toByteArray())
        response.outputStream.flush()
        response.outputStream.close()
    }
}
