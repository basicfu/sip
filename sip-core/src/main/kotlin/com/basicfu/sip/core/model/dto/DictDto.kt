package com.basicfu.sip.core.model.dto

class DictDto {
    var id: Long? = null
    var name: String? = null
    var value: String? = null
    var lft: Long? = null
    var rgt: Long? = null
    var lvl: Int? = null
    var pid: Long? = null
    var fixed:Int?=null
    var children:List<DictDto>?=null
}