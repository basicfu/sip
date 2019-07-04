package com.basicfu.sip.base.model.po

import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "user_auth")
class UserAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var uid: Long? = null
    var type: Int? = null
    var username: String? = null
    var password: String? = null
    var cdate: Int? = null
    var udate: Int? = null
    var ldate: Int? = null
}
