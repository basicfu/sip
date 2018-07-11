package com.basicfu.sip.base.model.dto

class UserDto {
    var id: Long? = null
    var tenantId: Long? = null
    var username: String? = null
    var mobile:String?=null
    var email:String?=null
    var content: String? = null
    var cdate: Int? = null
    var udate: Int? = null
    var ldate:Int?=null
    var type: Int? = null
    var status: Int? = null

    var roleIds:List<Long>?=null
    var menuIds:List<Long>?=null
    var permissionIds:List<Long>?=null
    var resourceIds:List<Long>?=null
}
