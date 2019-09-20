package com.basicfu.sip.base.model.vo

class ResourceVo {
    var id: Long? = null
    var appId: Long? = null
    var url: String? = null
    var method: String? = null
    var name: String? = null
    var cdate: Int? = null
    var udate: Int? = null

    var q: String? = null
    /**1为预览更新资源详情，2为真的要更新操作*/
    var type: Int? = null
}
