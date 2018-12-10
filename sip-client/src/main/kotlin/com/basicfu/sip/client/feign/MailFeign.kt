package com.basicfu.sip.client.feign

import com.alibaba.fastjson.JSONObject
import com.basicfu.sip.client.model.Result
import com.basicfu.sip.client.model.SendMailVo
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

/**
 * @author tianhu
 * @date 2018/7/20
 */
@FeignClient(value = "sip-notify", url = "\${sip.notify.url:}")
interface MailFeign {


    @PostMapping("/mailTmp/sendMail")
    fun sendMail(@RequestBody vo: SendMailVo): Result<JSONObject>
}
