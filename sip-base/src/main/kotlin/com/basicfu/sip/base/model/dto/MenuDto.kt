package com.basicfu.sip.base.model.dto

class MenuDto {
    var id: Long? = null
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

    var resourceCount: Long? = null
    var children: List<MenuDto>? = null
}
