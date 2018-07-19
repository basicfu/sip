package com.basicfu.sip.notify.service

import com.basicfu.sip.core.common.exception.CustomException
import com.basicfu.sip.core.common.mapper.example
import com.basicfu.sip.core.service.BaseService
import com.basicfu.sip.notify.common.Enum
import com.basicfu.sip.notify.mapper.MailSenderMapper
import com.basicfu.sip.notify.mapper.MailTemplateMapper
import com.basicfu.sip.notify.model.po.MailSender
import com.basicfu.sip.notify.model.po.MailTemplate
import com.basicfu.sip.notify.model.vo.MailUtilVo
import com.basicfu.sip.notify.model.vo.SendMailVo
import com.basicfu.sip.notify.util.EmailUtil
import org.apache.commons.collections.CollectionUtils
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.Executors

/**
 * @author tianhu
 * @date 2018/7/18
 */
@Service
class MailTemplateService : BaseService<MailTemplateMapper, MailTemplate>() {


    @Autowired
    lateinit var mailTemplateMapper: MailTemplateMapper

    @Autowired
    lateinit var mailSenderMapper: MailSenderMapper


    @Transactional
    fun sendMail(vo: SendMailVo): Boolean {
        //异步发送邮件
        return if (vo.async){
            val excutor = Executors.newCachedThreadPool()
            excutor.execute {
                dealSendMail(vo)
            }
            true
        }else{
            dealSendMail(vo)
        }
    }



    fun dealSendMail(vo: SendMailVo): Boolean{
        if (null == vo.appId) throw CustomException(Enum.INVALID_MAIL_APPID)
        val sendExample = example<MailSender> {
            andEqualTo(MailSender::appId,vo.appId)
        }
        val senders = mailSenderMapper.selectByExample(sendExample)
        if (CollectionUtils.isEmpty(senders)) throw CustomException(Enum.EMPTY_SENDER)
        val mailVo = MailUtilVo()
        //使用邮件模板
        if (vo.useTemplate!!) {
            val tempExample = example<MailTemplate> {
                andEqualTo(MailTemplate::appId,vo.appId)
                andEqualTo(MailTemplate::code,vo.code)
            }
            val template = mailTemplateMapper.selectOneByExample(tempExample) ?: throw CustomException(Enum.INVALID_MAIL_CODE)
            if (!template.enable!!) throw CustomException(Enum.ENABLE_TEMPLATE)
            //如果模板配置了发送者信息,使用该配置,未配置取第一个
            if (null == template.senderId){
                BeanUtils.copyProperties(senders[0],mailVo)
            }else{
                val filter = senders.filter { it.appId == template.senderId }
                BeanUtils.copyProperties(filter[0],mailVo)
            }
            val properties = vo.properties
            var content = if(template.content.isNullOrBlank()) throw CustomException(Enum.EMPTY_CONTENT_TEMPLATE) else template.content
            if (null ==  properties|| properties.isEmpty()) throw CustomException(Enum.EMPTY_CONTENT)
            //覆盖模板收件人抄送人
            return if (vo.cover){
                BeanUtils.copyProperties(vo,mailVo)
                mailVo.subject = template.subject
                mailVo.content = dealProperties(properties,content!!)
                EmailUtil.sendMail(mailVo)
            }else{
                mailVo.toUser = template.toUser!!.split(",").toTypedArray()
                mailVo.copyUser = template.copyUser!!.split(",").toTypedArray()
                mailVo.content = dealProperties(properties,content!!)
                mailVo.subject = template.subject
                EmailUtil.sendMail(mailVo)
            }
        } else {
            //不使用邮件模板
            if (vo.content.isNullOrBlank()) throw CustomException(Enum.EMPTY_CONTENT_TEMPLATE)
            BeanUtils.copyProperties(senders[0],mailVo)
            BeanUtils.copyProperties(vo,mailVo)
            return EmailUtil.sendMail(mailVo)
        }
    }


    fun dealProperties(properties: Map<String, String>,content: String): String{
        var str = content
        properties.forEach { k, v ->
            str = str.replace("\${$k}",v)
        }
        return str
    }

}