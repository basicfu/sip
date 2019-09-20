package com.basicfu.sip.base.model.po

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class Secret {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    @Column(name="`key`")
    var key: String? = null
    var secret: String? = null
    var cdate: Int? = null
    var udate: Int? = null
}
