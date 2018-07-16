package com.basicfu.sip.core.model.po

import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class App {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var name: String? = null
    var domain: String? = null
    var cdate: Int? = null
    var udate: Int? = null
}
