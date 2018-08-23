package com.basicfu.sip.logs.modal.po

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class Warn {
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
     * 规则名
     */
    /**
     * 获取规则名
     *
     * @return name - 规则名
     */
    /**
     * 设置规则名
     *
     * @param name 规则名
     */
    var name: String? = null

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
     * 创建时间
     */
    /**
     * 获取创建时间
     *
     * @return cdate - 创建时间
     */
    /**
     * 设置创建时间
     *
     * @param cdate 创建时间
     */
    var cdate: Int? = null

    /**
     * 更新时间
     */
    /**
     * 获取更新时间
     *
     * @return udate - 更新时间
     */
    /**
     * 设置更新时间
     *
     * @param udate 更新时间
     */
    var udate: Int? = null
}
