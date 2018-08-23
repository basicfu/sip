package com.basicfu.sip.logs.modal.po

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class Template {
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
     * 应用ID
     */
    /**
     * 获取应用ID
     *
     * @return app_id - 应用ID
     */
    /**
     * 设置应用ID
     *
     * @param appId 应用ID
     */
    @Column(name = "app_id")
    var appId: Long? = null

    /**
     * 模板名
     */
    /**
     * 获取模板名
     *
     * @return name - 模板名
     */
    /**
     * 设置模板名
     *
     * @param name 模板名
     */
    var name: String? = null

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
