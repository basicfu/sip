package com.basicfu.sip.logs.modal.po

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "warn_mode")
class WarnMode {
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
     * 报警项ID
     */
    /**
     * 获取报警项ID
     *
     * @return warn_id - 报警项ID
     */
    /**
     * 设置报警项ID
     *
     * @param warnId 报警项ID
     */
    @Column(name = "warn_id")
    var warnId: Long? = null

    /**
     * 报警模式(notify模块的所有类型(邮件、短信、webhook、微信/QQ))
     */
    /**
     * 获取报警模式(notify模块的所有类型(邮件、短信、webhook、微信/QQ))
     *
     * @return mode - 报警模式(notify模块的所有类型(邮件、短信、webhook、微信/QQ))
     */
    /**
     * 设置报警模式(notify模块的所有类型(邮件、短信、webhook、微信/QQ))
     *
     * @param mode 报警模式(notify模块的所有类型(邮件、短信、webhook、微信/QQ))
     */
    var mode: String? = null

    /**
     * 通知模式的模板ID
     */
    /**
     * 获取通知模式的模板ID
     *
     * @return template_id - 通知模式的模板ID
     */
    /**
     * 设置通知模式的模板ID
     *
     * @param templateId 通知模式的模板ID
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
