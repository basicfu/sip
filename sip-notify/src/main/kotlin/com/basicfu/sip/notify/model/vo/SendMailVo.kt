package com.basicfu.sip.notify.model.vo

/**
 * @author tianhu
 * @date 2018/7/18
 */
class SendMailVo {
    var appId: Long? = null
    /** useTemplate 为true时必传 */
    var code: String? = null
    var toUser: String? = null
    var toSendType: String? = null
    var copyUser: String? = null
    var copySendType: String? = null
    var subject: String? = null
    var description: String? = null
    /** useTemplate 为false时必传 */
    var content: String? = null

    var propertis: Map<String, String>? = null
    /** 是否开启异步 */
    var async: Boolean? = null
    /** 是否使用模板 */
    var useTemplate: Boolean? = null


}