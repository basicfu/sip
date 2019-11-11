package com.basicfu.sip.base.controller

import com.basicfu.sip.base.service.SipService
import com.basicfu.sip.base.service.UserService
import com.basicfu.sip.base.util.SmsUtil
import com.basicfu.sip.core.model.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sip")
class SipController {
    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var sipService: SipService

    @PostMapping("/permission/check")
    fun permissionCheck(@RequestBody map: HashMap<String, String>): Result<Any> {
        return sipService.permissionCheck(map)
    }

    @PostMapping("/sms")
    fun sms(@RequestBody map: HashMap<String, String>): Result<Any> {
        val type = map["type"]
        val mobile = map["mobile"]!!
        if (type != null) {
            userService.checkMobile(mobile, type)
        }
        SmsUtil.send(mobile)
        return Result.success(null, "发送成功")
    }

}
