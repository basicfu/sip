package com.basicfu.sip.permission.model.vo

class MenuVo {
    var id: Long? = null
    var appId: Long? = null
    var pid: Long? = null
    var name: String? = null
    var path: String? = null
    var sort: Int? = null
    var icon: String? = null
    var type: String? = null
    var display: Int? = null

    var resourceIds:List<Long>?=null
}
