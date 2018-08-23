package com.basicfu.sip.logs.modal.po

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "template_field")
class TemplateField {
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
     * 字段名
     */
    /**
     * 获取字段名
     *
     * @return name - 字段名
     */
    /**
     * 设置字段名
     *
     * @param name 字段名
     */
    var name: String? = null

    /**
     * 字段英文名
     */
    /**
     * 获取字段英文名
     *
     * @return en_name - 字段英文名
     */
    /**
     * 设置字段英文名
     *
     * @param enName 字段英文名
     */
    @Column(name = "en_name")
    var enName: String? = null

    /**
     * 字段顺序
     */
    /**
     * 获取字段顺序
     *
     * @return sort - 字段顺序
     */
    /**
     * 设置字段顺序
     *
     * @param sort 字段顺序
     */
    var sort: Int? = null

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
