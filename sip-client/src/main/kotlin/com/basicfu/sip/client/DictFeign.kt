package com.basicfu.sip.client

import com.basicfu.sip.core.model.Result
import com.basicfu.sip.core.model.dto.DictDto
import org.springframework.cloud.netflix.feign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(value = "sip-dict", url = "\${sip.dict.url:}")
interface DictFeign {

    @GetMapping("/get/{value}")
    fun get(@PathVariable value: String): Result<List<DictDto>>

}
