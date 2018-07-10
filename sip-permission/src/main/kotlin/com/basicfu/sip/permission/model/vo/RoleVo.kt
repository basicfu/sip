package com.basicfu.sip.permission.model.vo

class RoleVo {
    var id: Long? = null
    var appId: Long? = null
    var name: String? = null
    var enalbe: Int? = null

    var userIds:List<Long>?=null
    var menuIds:List<Long>?=null
    var permissionIds:List<Long>?=null
}
