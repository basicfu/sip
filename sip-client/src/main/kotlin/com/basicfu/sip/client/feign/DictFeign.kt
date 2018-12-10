package com.basicfu.sip.client.feign

import com.basicfu.sip.client.model.DictDto
import com.basicfu.sip.client.model.Result
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(value = "sip-dict", url = "\${sip.dict.url:}")
interface DictFeign {

    @GetMapping("/dict/get/{value}")
    fun get(@PathVariable("value") value: String): Result<List<DictDto>>

}
