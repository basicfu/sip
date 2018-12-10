package com.basicfu.sip.client.feign

import com.alibaba.fastjson.JSONArray
import com.basicfu.sip.client.model.Result
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(value = "sip-base", url = "\${sip.base.url:}")
interface ClientFeign {

    /**
     * 注入的sip-base其实无效，只是借助一个发起请求而已，在feignConfiguration中已修改
     */
    @GetMapping("/sip/client/url")
    fun sipClientUrl(@RequestParam("service") service: String): Result<JSONArray>

}
