package com.basicfu.sip.base.model.dto

class AppDto {
    var id: Long? = null
    var name: String? = null
    var code: String? = null
    var cdate: Int? = null
    var udate: Int? = null

    var services: List<AppServiceDto>? = null
    var secrets: List<AppSecretDto>? = null
}
