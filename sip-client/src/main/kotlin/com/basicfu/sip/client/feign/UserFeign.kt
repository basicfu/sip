package com.basicfu.sip.client.feign

import com.alibaba.fastjson.JSONObject
import com.basicfu.sip.client.model.Result
import org.springframework.cloud.netflix.feign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(value = "sip-base", url = "\${sip.base.url:}")
interface UserFeign {

    @GetMapping("/user")
    fun getCurrentUser(): Result<JSONObject>

    @GetMapping("/user/get/{id}")
    fun get(@PathVariable("id") id: Long): Result<JSONObject>

    @GetMapping("/user/get/permission/{id}")
    fun getPermission(@PathVariable("id") id: Long): Result<JSONObject>

    @GetMapping("/user/list/{ids}")
    fun listByIds(@PathVariable("ids") ids: Array<Long>): Result<List<JSONObject>>

    @GetMapping("/user/list/username/{ids}")
    fun listUsernameByIds(@PathVariable("ids") ids: Array<Long>): Result<List<JSONObject>>

    @GetMapping("/user/suggest/{name}")
    fun suggest(
        @PathVariable("name") name: String,
        @RequestParam(value = "roleCode", required = false) roleCode: String?,
        @RequestParam(value = "limit", required = false) limit: Int
    ): Result<List<JSONObject>>

}
