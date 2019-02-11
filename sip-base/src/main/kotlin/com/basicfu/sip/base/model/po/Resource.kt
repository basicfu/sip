package com.basicfu.sip.base.model.po

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    @Column(name = "app_id")
    var appId: Long? = null
    @Column(name = "service_id")
    var serviceId: Long? = null
    var url: String? = null
    var name: String? = null
    var method: String? = null
    var cdate: Int? = null
    var udate: Int? = null
}
