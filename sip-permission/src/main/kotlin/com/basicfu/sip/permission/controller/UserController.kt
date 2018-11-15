package com.basicfu.sip.permission.controller

import com.basicfu.sip.common.model.Result
import com.basicfu.sip.permission.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * @author basicfu
 * @date 2018/9/25
 */
@RestController
@RequestMapping("/user")
class UserController {
    @Autowired
    lateinit var userService: UserService

    @GetMapping("/list/role/{ids}")
    fun listRoleByIds(@PathVariable ids: List<Long>): Result<Any> {
        return Result.success(userService.listRoleByIds(ids))
    }

    @PostMapping("/update/role")
    fun updateRole(@RequestParam("id") id: Long, @RequestParam("roleIds") roleIds: Array<Long>): Result<Any> {
        return Result.success(userService.updateRole(id, roleIds.toList()))
    }
}
