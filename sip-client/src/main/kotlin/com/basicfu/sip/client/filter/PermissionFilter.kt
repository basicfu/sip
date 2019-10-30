package com.basicfu.sip.client.filter

import com.alibaba.fastjson.JSON
import com.basicfu.sip.client.util.SipUtil
import org.springframework.http.HttpStatus
import javax.servlet.*
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * 内置权限过滤器
 * 可配置是否开启内置过滤器，或自己实现
 */
@WebFilter(filterName = "permissionFilter", urlPatterns = ["/*"])
class PermissionFilter : Filter {
    override fun init(filterConfig: FilterConfig) {
    }

    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {
        val request = servletRequest as HttpServletRequest
        val response = servletResponse as HttpServletResponse
        val permissionResult = SipUtil.checkPermission(request.method, request.requestURI)
        if (permissionResult.success!!) {
            filterChain.doFilter(request, servletResponse)
        } else {
            returnMsg(response, JSON.toJSONString(permissionResult))
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
