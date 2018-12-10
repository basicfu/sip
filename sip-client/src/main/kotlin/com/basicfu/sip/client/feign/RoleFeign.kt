package com.basicfu.sip.client.feign

import com.alibaba.fastjson.JSONObject
import com.basicfu.sip.client.model.Result
import com.basicfu.sip.client.model.UserDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(value = "sip-permission", url = "\${sip.permission.url:}")
interface RoleFeign {
    @GetMapping("/role/list/{uid}/role")
    fun listRoleByUid(@PathVariable("uid") uid: Long): Result<List<String>>

    @GetMapping("/user/list/role/{ids}")
    fun listRoleByIds(@PathVariable("ids") ids: Array<Long>): Result<List<UserDto>>

    @PostMapping("/user/update/role")
    fun updateRole(@RequestParam("id") id: Long, @RequestParam("roleIds") roleIds: Array<Long>): Result<JSONObject>
}
