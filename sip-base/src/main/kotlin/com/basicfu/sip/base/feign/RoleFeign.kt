package com.basicfu.sip.base.feign

import com.basicfu.sip.client.model.Result
import com.basicfu.sip.common.model.dto.MenuDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(value = "sip-permission", url = "\${sip.permission.url:}")
interface RoleFeign {
    @GetMapping("/menu/list/{ids}")
    fun listMenuByIds(@PathVariable("ids") ids:Array<Long>): Result<List<MenuDto>>
}
