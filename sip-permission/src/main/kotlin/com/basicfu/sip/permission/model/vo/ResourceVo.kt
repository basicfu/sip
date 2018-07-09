package com.basicfu.sip.permission.model.vo

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class ResourceVo {
    var id: Long? = null
    var serviceId: Long? = null
    var url: String? = null
    var name: String? = null

    var keyword:String?=null
}
