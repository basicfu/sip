package com.basicfu.sip.core.model.vo

class DictVo {
    var id: Long? = null
    var name: String? = null
    var value: String? = null
    var description: String? = null
    var lft: Long? = null
    var rgt: Long? = null
    var lvl: Int? = null
    var fixed: Int? = null
    var isdel: Int? = null

    /**父级pid*/
    var pid: Long? = null
}
