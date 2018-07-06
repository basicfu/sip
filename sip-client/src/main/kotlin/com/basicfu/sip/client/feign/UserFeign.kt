package com.basicfu.sip.client.feign

import com.basicfu.sip.client.model.UserDto
import org.springframework.cloud.netflix.feign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(value = "sip-base", url = "\${sip.base.url:}")
interface UserFeign {

    @GetMapping("/user/get/{id}")
    fun get(@PathVariable("id") id: Long): UserDto?

}
