package com.basicfu.sip.permission.model.vo

class PermissionVo {
    var id: Long? = null
    var appId: Long? = null
    var name: String? = null
    var code: String? = null

    /**导入的value*/
    var value:String?=null
    var q: String? = null
    var resourceIds: List<Long>? = null
}
