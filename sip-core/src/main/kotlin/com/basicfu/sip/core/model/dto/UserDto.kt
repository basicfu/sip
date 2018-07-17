package com.basicfu.sip.core.model.dto

class UserDto {
    var id: Long? = null
    var appId: Long? = null
    var username: String? = null
    var mobile: String? = null
    var email: String? = null
    var content: String? = null
    var cdate: Int? = null
    var udate: Int? = null
    var ldate: Int? = null
    var type: Int? = null
    var status: Int? = null

    var roleCodes: List<String>? = null
    var menuIds: List<Long>? = null
    var permissionIds: List<Long>? = null
    var resources:Map<Long,List<String>>? = null
}
