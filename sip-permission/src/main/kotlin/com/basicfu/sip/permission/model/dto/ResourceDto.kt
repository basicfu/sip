package com.basicfu.sip.permission.model.dto

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class ResourceDto {

    var id: Long? = null
    var serviceId: Long? = null
    var url: String? = null
    var name: String? = null
}
