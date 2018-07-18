package com.basicfu.sip.core.model.dto

import com.alibaba.fastjson.JSONArray
import com.basicfu.sip.core.model.po.Resource

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

    var roles: JSONArray? = null
    var menus: JSONArray? = null
    var permissions: JSONArray? = null
    var resources: Map<String, List<String>>? = null
}
