package com.basicfu.sip.logs.modal.po

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class Logs {
    /**
     * @return id
     */
    /**
     * @param id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    /**
     * 模板ID
     */
    /**
     * 获取模板ID
     *
     * @return template_id - 模板ID
     */
    /**
     * 设置模板ID
     *
     * @param templateId 模板ID
     */
    @Column(name = "template_id")
    var templateId: Long? = null

    /**
     * 创建时间(ms)
     */
    /**
     * 获取创建时间(ms)
     *
     * @return cdate - 创建时间(ms)
     */
    /**
     * 设置创建时间(ms)
     *
     * @param cdate 创建时间(ms)
     */
    var cdate: Long? = null

    /**
     * 日志json内容
     */
    /**
     * 获取日志json内容
     *
     * @return content - 日志json内容
     */
    /**
     * 设置日志json内容
     *
     * @param content 日志json内容
     */
    var content: String? = null
}
