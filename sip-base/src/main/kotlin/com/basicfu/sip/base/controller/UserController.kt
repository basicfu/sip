package com.basicfu.sip.base.controller

import com.basicfu.sip.base.model.vo.UserVo
import com.basicfu.sip.base.service.UserService
import com.basicfu.sip.common.constant.Constant
import com.basicfu.sip.core.model.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * @author basicfu
 * @date 2018/7/4
 */
@RestController
@RequestMapping("/user")
class UserController {
    @Autowired
    lateinit var userService: UserService

    @GetMapping
    fun getCurrentUser(): Result<Any> {
        return Result.success(userService.getCurrentUser())
    }

    @GetMapping("/get/{id}")
    fun get(@PathVariable id: Long): Result<Any> {
        return Result.success(userService.get(id))
    }

    @GetMapping("/list")
    fun list(vo: UserVo): Result<Any> {
        return Result.success(userService.list(vo))
    }

    @GetMapping("/list/{ids}")
    fun listByIds(@PathVariable ids: List<Long>): Result<Any> {
        return Result.success(userService.listByIds(ids))
    }

    @GetMapping("/list/username/{ids}")
    fun listUsernameByIds(@PathVariable ids: List<Long>): Result<Any> {
        return Result.success(userService.listUsernameByIds(ids))
    }

    @GetMapping("/suggest/{name}")
    fun suggest(
        @PathVariable name: String,
        @RequestParam(required = false) roleCode: String?,
        @RequestParam(defaultValue = Constant.System.PAGE_SIZE_STR) limit: Int
    ): Result<Any> {
        return Result.success(userService.suggest(name, roleCode, limit))
    }

    @PostMapping("/login")
    fun login(@RequestBody vo: UserVo): Result<Any> {
        return Result.success(userService.login(vo))
    }

    @GetMapping("/logout")
    fun logout(): Result<Any> {
        return Result.success(userService.logout(), Constant.System.LOGOUT)
    }

    @PostMapping("/insert")
    fun insert(@RequestBody map: Map<String, Any>): Result<Any> {
        return Result.success(userService.insert(map))
    }

    @PostMapping("/register")
    fun register(@RequestBody map: Map<String, Any>): Result<Any> {
        return Result.success(userService.insert(map))
    }

    @PostMapping("/update")
    fun update(@RequestBody map: Map<String, Any>): Result<Any> {
        return Result.success(userService.update(map))
    }

    @PostMapping("/update/password")
    fun update(@RequestBody vo: UserVo): Result<Any> {
        userService.updatePassword(vo)
        return Result.success("修改成功")
    }

    @DeleteMapping("/delete")
    fun delete(@RequestBody ids: List<Long>): Result<Any> {
        return Result.success(userService.delete(ids))
    }

    @GetMapping("/list/role/{ids}")
    fun listRoleByIds(@PathVariable ids: List<Long>): Result<Any> {
        return Result.success(userService.listRoleByIds(ids))
    }
}
