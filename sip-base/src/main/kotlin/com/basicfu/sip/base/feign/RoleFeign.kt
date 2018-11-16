package com.basicfu.sip.base.feign

import com.basicfu.sip.client.model.Result
import com.basicfu.sip.common.model.dto.MenuDto
import org.springframework.cloud.netflix.feign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(value = "sip-permission", url = "\${sip.permission.url:}")
interface RoleFeign {
    @GetMapping("/menu/all")
    fun allMenu(): Result<List<MenuDto>>
}
