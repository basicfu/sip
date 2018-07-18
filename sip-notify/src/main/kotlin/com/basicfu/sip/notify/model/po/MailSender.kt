package com.basicfu.sip.notify.model.po

import javax.persistence.*

@Table(name = "mail_sender")
class MailSender {
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
     * 设置发件人用户名
     *
     * @param fromUname 发件人用户名
     */
    @Column(name = "from_uname")
    var fromUname: String? = null
    /**
     * 设置发件人密码
     *
     * @param fromUpwd 发件人密码
     */
    @Column(name = "from_upwd")
    var fromUpwd: String? = null
    /**
     * 设置描述
     *
     * @param description 描述
     */
    var description: String? = null
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
}