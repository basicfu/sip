package com.basicfu.sip.base.controller

import com.basicfu.sip.base.service.SipService
import com.basicfu.sip.base.service.UserService
import com.basicfu.sip.base.util.SmsUtil
import com.basicfu.sip.core.model.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/sip")
class SipController {
    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var sipService: SipService

    @PostMapping("/permission/check")
    fun permissionCheck(@RequestBody map:HashMap<String,String>): Result<Any>{
        return sipService.permissionCheck(map)
    }

    @PostMapping("/sms/{mobile}")
    fun sms(@PathVariable mobile: String): Result<Any> {
        userService.checkMobile(mobile)
        SmsUtil.send(mobile)
        return Result.success(null, "发送成功")
    }

}
