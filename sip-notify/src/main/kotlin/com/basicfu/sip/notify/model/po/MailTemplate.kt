package com.basicfu.sip.notify.model.po

import javax.persistence.*

@Table(name = "mail_template")
class MailTemplate {
    /**
     * @param id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    /**
     * 设置应用id
     *
     * @param appId 应用id
     */
    @Column(name = "app_id")
    var appId: Long? = null
    /**
     * 设置发送者id
     *
     * @param senderId 发送者id
     */
    @Column(name = "sender_id")
    var senderId: Long? = null
    /**
     * 设置邮件模板code
     *
     * @param code 邮件模板code
     */
    var code: String? = null
    /**
     * 设置收件人,多个收件人使用 , 分割
     *
     * @param toUser 收件人,多个收件人使用 , 分割
     */
    @Column(name = "to_user")
    var toUser: String? = null
    /**
     * 设置收件人发送类型,alone:单独发送,mass:群发
     *
     * @param toSendType 收件人发送类型,alone:单独发送,mass:群发
     */
    @Column(name = "send_type")
    var sendType: String? = null
    /**
     * 设置抄送人
     *
     * @param copyUser 抄送人
     */
    @Column(name = "copy_user")
    var copyUser: String? = null
    /**
     * 设置邮件主题
     *
     * @param subject 邮件主题
     */
    var subject: String? = null
    /**
     * 设置邮件描述
     *
     * @param description 邮件描述
     */
    var description: String? = null
    /**
     * 设置是否启用,0:禁用,1:启用
     *
     * @param enable 是否启用,0:禁用,1:启用
     */
    var enable: Boolean? = null
    /**
     * 设置创建时间
     *
     * @param cdate 创建时间
     */
    var cdate: Int? = null
    /**
     * 设置更新时间
     *
     * @param udate 更新时间
     */
    var udate: Int? = null
    /**
     * 设置邮件正文
     *
     * @param content 邮件正文
     */
    var content: String? = null
}