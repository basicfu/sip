package com.basicfu.sip.permission.feign

import com.basicfu.sip.client.model.Result
import com.basicfu.sip.common.model.dto.AppServiceDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(value = "sip-base", url = "\${sip.base.url:}")
interface AppServiceFeign {
    @GetMapping("/app-service/all")
    fun all(@RequestParam("type") type: Int = 0): Result<List<AppServiceDto>>
}
