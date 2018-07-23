package com.basicfu.sip.client.model

import java.io.File

/**
 * @author tianhu
 * @date 2018/7/18
 */
class SendMailVo {
    var appId: Long? = null
    /** useTemplate 为true时必传 */
    var code: String? = null
    /** 收件人  */
    var toUser: Array<String>? = null
    /** 抄送人  */
    var copyUser: Array<String>? = null
    var sendType: String? = null
    var subject: String? = null
    var description: String? = null
    /** useTemplate 为false时必传 */
    var content: String? = null

    var properties: Map<String, String>? = null
    /** 附件  */
    var files: List<File>? = null
    /** 内嵌图片  */
    var images: Map<String, File>? = null
    /** 是否开启异步 */
    var async: Boolean = false
    /** 是否使用模板 */
    var useTemplate: Boolean? = null
    /** useTemplate 为true时必传 是否覆盖模板内容 */
    var cover: Boolean = false


}