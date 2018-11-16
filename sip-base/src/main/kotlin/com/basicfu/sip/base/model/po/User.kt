package com.basicfu.sip.base.model.po

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    @Column(name = "app_id")
    var appId: Long? = null
    var username: String? = null
    var nickname: String? = null
    var mobile: String? = null
    var email: String? = null
    var content: String? = null
    var cdate: Int? = null
    var udate: Int? = null
    var cuid: Long? = null
    var type: String? = null
    var status: Int? = null
}
