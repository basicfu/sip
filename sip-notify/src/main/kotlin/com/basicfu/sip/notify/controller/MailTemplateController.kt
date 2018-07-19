package com.basicfu.sip.notify.controller

import com.alibaba.fastjson.JSON
import com.basicfu.sip.notify.model.vo.SendMailVo
import com.basicfu.sip.notify.service.MailTemplateService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * @author tianhu
 * @date 2018/7/18
 */
@RestController
@RequestMapping("/template")
class MailTemplateController{

    @Autowired
    lateinit var mailTemplateService: MailTemplateService

    @PostMapping("/sendMail")
    fun sendMail(@RequestBody vo: SendMailVo): Boolean{
        return mailTemplateService.sendMail(vo)
    }
}

fun main(args: Array<String>) {
    val vo = SendMailVo()
    vo.useTemplate = false
    vo.appId = 1
    vo.toUser = arrayOf("18910751785@163.com")
    vo.content = "哈哈哈哈哈哈"
    vo.subject = "策士"
    print(JSON.toJSONString(vo))

}