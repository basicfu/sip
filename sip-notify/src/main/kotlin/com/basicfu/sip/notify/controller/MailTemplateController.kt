package com.basicfu.sip.notify.controller

import com.alibaba.fastjson.JSON
import com.basicfu.sip.core.model.Result
import com.basicfu.sip.notify.model.vo.SendMailVo
import com.basicfu.sip.notify.service.MailTemplateService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author tianhu
 * @date 2018/7/18
 */
@RestController
@RequestMapping("/mailTmp")
class MailTemplateController {

    @Autowired
    lateinit var mailTemplateService: MailTemplateService

    @PostMapping("/sendMail")
    fun sendMail(@RequestBody vo: SendMailVo): Result<Any> {
        print(JSON.toJSONString(vo))
        return Result.success(mailTemplateService.insert(vo))
    }

}
