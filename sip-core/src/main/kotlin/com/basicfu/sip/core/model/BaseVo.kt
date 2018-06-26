package com.basicfu.sip.core.model

import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

open class BaseVo {
    var pageNum: Int = 1
    var pageSize: Int = 20
        set(value) {
            field = if (pageSize > 100) 100 else value
        }

    /**
     * 自动设置当前请求参数中的分页信息
     */
    fun setInfo(): BaseVo {
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        val pageNum = request.getParameter("pageNum")
        val pageSize = request.getParameter("pageSize")
        if (pageNum != null) {
            this.pageNum = pageNum.toInt()
        }
        if (pageSize != null) {
            this.pageSize = pageSize.toInt()
        }
        return this
    }
}
