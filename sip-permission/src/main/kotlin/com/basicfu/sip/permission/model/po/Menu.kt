package com.basicfu.sip.permission.model.po

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    @Column(name = "app_id")
    var appId: Long? = null
    var pid: Long? = null
    var name: String? = null
    var path: String? = null
    var sort: Int? = null
    var icon: String? = null
    var type: String? = null
    var cdate: Int? = null
    var udate: Int? = null
    var display: Boolean? = null
}
