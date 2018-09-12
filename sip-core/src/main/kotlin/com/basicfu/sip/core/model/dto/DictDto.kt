package com.basicfu.sip.core.model.dto

class DictDto {
    var id: Long? = null
    var appId: Long? = null
    var name: String? = null
    var value: String? = null
    var description: String? = null
    var lft: Long? = null
    var rgt: Long? = null
    var lvl: Int? = null
    var sort:Int?=null
    var fixed:Boolean?=null
    var children:List<DictDto>?=null
}
