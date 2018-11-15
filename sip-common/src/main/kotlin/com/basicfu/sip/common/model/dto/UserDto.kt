package com.basicfu.sip.common.model.dto

import com.alibaba.fastjson.JSONArray

class UserDto {
    var id: Long? = null
    var appId: Long? = null
    var username: String? = null
    var nickname: String? = null
    var mobile: String? = null
    var email: String? = null
    var content: String? = null
    var cdate: Int? = null
    var udate: Int? = null
    var cuid: Long? = null
    var ldate: Int? = null
    var type: String? = null
    var status: Int? = null

    var cuname: String? = null
    var appCode: String? = null
    var token: String? = null

    var roles: JSONArray? = null
    var menus: JSONArray? = null
    var permissions: JSONArray? = null
    var resources: Map<String, List<String>>? = null
}
