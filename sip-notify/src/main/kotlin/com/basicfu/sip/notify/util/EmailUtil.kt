package com.basicfu.sip.notify.util

import com.basicfu.sip.notify.model.vo.MailUtilVo
import org.aspectj.bridge.MessageUtil
import org.slf4j.LoggerFactory
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.mail.javamail.MimeMessageHelper
import java.io.File
import java.util.*
import javax.activation.MimetypesFileTypeMap
import javax.mail.internet.InternetAddress

/**
 * @author tianhu
 * @date 2018/2/26
 */
object EmailUtil {
    private val logger = LoggerFactory.getLogger(MessageUtil::class.java)


    /**
     * Description: 发送携带附件和图片的邮件
     * @param receivers 收件人邮箱
     * @param cc 抄送人邮箱
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param attachments 附件
     * @param inlineImgs 邮件中嵌套的图片
     */
    fun sendMail(vo: MailUtilVo): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        map["success"] = true
        val senderImpl = JavaMailSenderImpl()
        try {
            // 设定mail server
            senderImpl.host = vo.host
            senderImpl.port = vo.port!!
            // 建立邮件消息
            val mailMessage = senderImpl.createMimeMessage()
            var messageHelper: MimeMessageHelper? = null
            messageHelper = MimeMessageHelper(mailMessage, true, "UTF-8")
            // 设置发件人邮箱
            //            messageHelper.setFrom(from)
            messageHelper.setFrom(InternetAddress(javax.mail.internet.MimeUtility.encodeText(vo.fromName) + " <" + vo.fromUname + ">"))
            // 设置收件人邮箱
            messageHelper.setTo(vo.toUser)
            // 设置抄送人
            vo.copyUser?.let { messageHelper.setCc(vo.copyUser) }
            //设置暗送人
//            messageHelper.setBcc(bcc)
            // 邮件主题
            messageHelper.setSubject(vo.subject)
            // true 表示启动HTML格式的邮件
            messageHelper.setText(vo.content, true)
            // 设置附件
            vo.files?.let {
                for (file in vo.files!!) {
                    if (!checkFileType(file)) continue
                    logger.info("===================添加附件对象:" + file.name)
                    messageHelper.addAttachment(file.name, file)
                }
            }
            // 设置嵌套图片
            vo.images?.let {
                for (key in vo.images!!.keys) {
                    val file = vo.images!![key]
                    logger.info("===================添加嵌套图片key:" + key + "---fileName:" + file!!.name)
                    messageHelper.addInline(key, file)
                }
            }
            senderImpl.username = vo.fromUname
            senderImpl.password = vo.fromUpwd
            val prop = Properties()
//            prop["mail.smtp.port"] = vo.port
//            prop["mail.smtp.auth"] = "true" // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
//            prop["mail.smtp.timeout"] = "6000"
            // SSL
            prop["mail.smtp.auth "] = "true" // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
            prop["mail.smtp.starttls.enable"] = "true"
            prop["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory" //使用ssl协议来保证连接安全
            prop["mail.smtp.timeout"] = "25000" //传输超时时间
            senderImpl.javaMailProperties = prop
            // 发送邮件
            senderImpl.send(mailMessage)
            logger.info("=============发送邮件成功=============")
        } catch (e: Exception) {
            logger.info("=============发送邮件失败，异常信息为：$e.message")
            map["success"] = false
            map["message"] = e.message!!
            return map
        }
        return map
    }

    private fun checkFileType(file: File): Boolean {
        if ("application/octet-stream" == MimetypesFileTypeMap().getContentType(file) && file.name.endsWith(".exe")) {
            logger.info("=============构造邮件附件=============文件：$file.name 是可执行文件。出于安全性考虑，不允许添加此文件。")
            return false
        }
        return true
    }

}

