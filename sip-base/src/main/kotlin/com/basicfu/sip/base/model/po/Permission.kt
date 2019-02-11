package com.basicfu.sip.base.model.po

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    @Column(name = "app_id")
    var appId: Long? = null
    var name: String? = null
    var code: String? = null
    var cdate: Int? = null
    var udate: Int? = null
}
