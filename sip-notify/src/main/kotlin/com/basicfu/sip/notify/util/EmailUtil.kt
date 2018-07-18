//package com.basicfu.sip.notify.util
//
//import org.aspectj.bridge.MessageUtil
//import org.slf4j.LoggerFactory
//import org.springframework.mail.javamail.JavaMailSenderImpl
//import org.springframework.mail.javamail.MimeMessageHelper
//import java.io.File
//import java.util.*
//import javax.activation.MimetypesFileTypeMap
//import javax.mail.internet.InternetAddress
//
///**
// * @author tianhu
// * @date 2018/2/26
// */
//object EmailUtil {
//    private val logger = LoggerFactory.getLogger(MessageUtil::class.java)
//
//
//    /**
//     * Description: 发送携带附件和图片的邮件
//     * @param receivers 收件人邮箱
//     * @param cc 抄送人邮箱
//     * @param subject 邮件主题
//     * @param content 邮件内容
//     * @param attachments 附件
//     * @param inlineImgs 邮件中嵌套的图片
//     */
//    fun sendMail(subject: String, content: String, receivers: Array<String>, cc: Array<String>?,attachments: List<File>?, inlineImgs: Map<String, File>?):Boolean {
//        val senderImpl = JavaMailSenderImpl()
//        try {
//            // 设定mail server
//            senderImpl.host = Constants.MAIL_SERVER
//            // 建立邮件消息
//            val mailMessage = senderImpl.createMimeMessage()
//            var messageHelper: MimeMessageHelper? = null
//            messageHelper = MimeMessageHelper(mailMessage, true, "UTF-8")
//            // 设置发件人邮箱
//            //            messageHelper.setFrom(from)
//            messageHelper.setFrom(InternetAddress(javax.mail.internet.MimeUtility.encodeText(Constants.MAIL_FROMNAME) + " <" + Constants.MAIL_UNAME + ">"))
//            // 设置收件人邮箱
//            messageHelper.setTo(receivers)
//            // 设置抄送人
//            cc?.let { messageHelper.setCc(cc) }
//            //设置暗送人
////            messageHelper.setBcc(bcc)
//            // 邮件主题
//            messageHelper.setSubject(subject)
//            // true 表示启动HTML格式的邮件
//            messageHelper.setText(content, true)
//            // 设置附件
//            attachments?.let {
//                for (file in attachments) {
//                    if (!checkFileType(file)) continue
//                    logger.info("===================添加附件对象:" + file.name)
//                    messageHelper.addAttachment(file.name, file)
//                }
//            }
//            // 设置嵌套图片
//            inlineImgs?.let {
//                for (key in inlineImgs.keys) {
//                    val file = inlineImgs[key]
//                    logger.info("===================添加嵌套图片key:" + key + "---fileName:" + file!!.name)
//                    messageHelper.addInline(key, file)
//                }
//            }
//            senderImpl.username = Constants.MAIL_UNAME
//            senderImpl.password = Constants.MAIL_PWD
//            val prop = Properties()
//            prop["mail.smtp.port"] = Constants.MAIL_PORT
//            prop["mail.smtp.auth"] = "true" // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
//            prop["mail.smtp.timeout"] = Constants.MAIL_TIMEOUT
//            // 如果是QQ邮箱需要这样做 密码为加密后的
////            prop["mail.smtp.starttls.enable"] = "true"
////            prop["mail.smtp.socketFactory.port"] = "465"
////            prop["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
////            prop["mail.smtp.socketFactory.fallback"] = "false"
//            senderImpl.javaMailProperties = prop
//            // 发送邮件
//            senderImpl.send(mailMessage)
//            logger.info("=============发送邮件成功=============")
//        } catch (e: Exception) {
//            logger.info("=============发送邮件失败，异常信息为：$e.message")
//            return false
//        }
//        return true
//    }
//
//    private fun checkFileType(file: File): Boolean {
//        if ("application/octet-stream" == MimetypesFileTypeMap().getContentType(file) && file.name.endsWith(".exe")) {
//            logger.info("=============构造邮件附件=============文件：$file.name 是可执行文件。出于安全性考虑，不允许添加此文件。")
//            return false
//        }
//        return true
//    }
//
//}
