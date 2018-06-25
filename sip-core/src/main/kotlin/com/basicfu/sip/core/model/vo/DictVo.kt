package com.basicfu.sip.core.model.vo

import com.basicfu.sip.core.model.BaseVo

class DictVo: BaseVo() {
    var id: Long? = null
    var name: String? = null
    var value: String? = null
    var lft: Long? = null
    var rgt: Long? = null
    var lvl: Int? = null
    var fixed: Int? = null
    var isdel: Int? = null
}