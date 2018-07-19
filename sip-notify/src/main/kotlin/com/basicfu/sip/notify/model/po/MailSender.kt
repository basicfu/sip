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
    @Column(name = "app_id")
    var appId: Long? = null
    @Column(name = "from_uname")
    var fromUname: String? = null
    @Column(name = "from_upwd")
    var fromUpwd: String? = null
    @Column(name = "from_name")
    var fromName: String? = null
    var host: String? = null
    var port: Int? = null
    var description: String? = null
    var cdate: Int? = null
    var udate: Int? = null
}