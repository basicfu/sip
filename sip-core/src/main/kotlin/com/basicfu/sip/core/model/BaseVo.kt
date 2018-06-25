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
        this.pageNum = request.getParameter("pageNum").toInt()
        this.pageSize = request.getParameter("pageSize").toInt()
        return this
    }
}
