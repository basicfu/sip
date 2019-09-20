package com.basicfu.sip.base.model.dto

class UserDto {
    var id: String? = null
    var username: String? = null
    var nickname: String? = null
    var mobile: String? = null
    var email: String? = null
    var password: String? = null
    var mobileVerified: Boolean? = null
    var emailVerified: Boolean? = null
    var avatar: String? = null
    var createTime: Long? = null
    var updateTime: Long? = null
    var registerType: Int? = null
    var blocked: Boolean? = null
    var isdel: Boolean? = null

    var roles: List<String> = emptyList()
}
