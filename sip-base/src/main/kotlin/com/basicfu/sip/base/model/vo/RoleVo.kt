package com.basicfu.sip.base.model.vo

class RoleVo {
    var id: Long? = null
    var name: String? = null
    var code: String? = null
    var enable: Boolean? = null

    var q:String?=null
    var userIds:List<String>?=null
    var menuIds:List<Long>?=null
    var permissionIds:List<Long>?=null
}
