package com.basicfu.sip.common.model.vo

class DictVo {
    var id: Long? = null
    var name: String? = null
    var value: String? = null
    var description: String? = null
    var lft: Long? = null
    var rgt: Long? = null
    var lvl: Int? = null
    var sort: Int? = null
    var fixed: Boolean? = null
    var isdel: Boolean? = null

    /**父级pid*/
    var pid: Long? = null
    var splitDelimiter: String? = null
}
