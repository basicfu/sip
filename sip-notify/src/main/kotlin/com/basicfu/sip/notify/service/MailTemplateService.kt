package com.basicfu.sip.notify.service

import com.basicfu.sip.core.common.exception.CustomException
import com.basicfu.sip.core.service.BaseService
import com.basicfu.sip.notify.mapper.MailSenderMapper
import com.basicfu.sip.notify.mapper.MailTemplateMapper
import com.basicfu.sip.notify.model.po.MailSender
import com.basicfu.sip.notify.model.po.MailTemplate
import com.basicfu.sip.notify.model.vo.SendMailVo
import org.apache.commons.collections.CollectionUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tk.mybatis.mapper.entity.Example
import tk.mybatis.mapper.util.Sqls

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
    fun sendMail(vo: SendMailVo) {
        val sendExample = Example.builder(MailSender::class.java)
                .where(Sqls.custom().andEqualTo("appId", vo.appId))
                .build()
        val senders = mailSenderMapper.selectByExample(sendExample)
        if (CollectionUtils.isEmpty(senders)) throw CustomException("")
        //使用邮件模板
        if (vo.useTemplate!!) {
            val tempExample = Example.builder(MailTemplate::class.java)
                    .where(Sqls.custom().andEqualTo("appId", vo.appId).andEqualTo("code", vo.code))
                    .build()
            val template = mailTemplateMapper.selectOneByExample(tempExample) ?: throw CustomException("")
        } else {
            //不使用邮件模板
        }
    }


}