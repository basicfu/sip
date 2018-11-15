package com.basicfu.sip.common.model.po

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class Dict {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    @Column(name = "app_id")
    var appId: Long? = null
    var name: String? = null
    var value: String? = null
    var description: String? = null
    var lft: Long? = null
    var rgt: Long? = null
    var lvl: Int? = null
    var sort: Int? = null
    var fixed: Boolean? = null
    var isdel: Boolean? = null
}
