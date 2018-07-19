package com.basicfu.sip.notify.model.vo

import java.io.File

/**
 * @author tianhu
 * @date 2018/7/19
 */
class MailUtilVo{
    var fromUname: String? = null
    var fromUpwd: String? = null
    var fromName: String? = null
    var host: String? = null
    var port: Int? = null



    var subject: String? = null
    /** 收件人  */
    var toUser: Array<String>? = null
    /** 抄送人  */
    var copyUser: Array<String>? = null
    var content: String? = null
    /** 附件  */
    var files: List<File>? = null
    /** 内嵌图片  */
    var images: Map<String, File>? = null
}