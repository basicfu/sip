package com.basicfu.sip.core.model.dto

class ApplicationDto {
    var id: Long? = null
    var tenantId: Long? = null
    var name: String? = null
    var path: String? = null
    var serverId: String? = null
    var url: String? = null
    var stripPrefix: Boolean? = null
    var retryable: Boolean? = null
    var sensitiveHeaders: String? = null
}