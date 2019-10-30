package com.basicfu.sip.base.controller

import com.basicfu.sip.base.model.vo.UserVo
import com.basicfu.sip.base.service.UserService
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
    fun status(type:String?): Result<Any> {
        return Result.success(userService.status(type))
    }


    @GetMapping("/get")
    fun get(vo: UserVo): Result<Any> {
        return Result.success(userService.get(vo))
    }

    @GetMapping("/list")
    fun list(vo: UserVo): Result<Any> {
        return Result.success(userService.list(vo))
    }

    //
//    @GetMapping("/list/{ids}")
//    fun listByIds(@PathVariable ids: List<Long>): Result<Any> {
//        return Result.success(userService.listByIds(ids))
//    }
//
//    @GetMapping("/list/username/{ids}")
//    fun listUsernameByIds(@PathVariable ids: List<Long>): Result<Any> {
//        return Result.success(userService.listUsernameByIds(ids))
//    }
//
//    @GetMapping("/suggest/{name}")
//    fun suggest(
//        @PathVariable name: String,
//        @RequestParam(required = false) roleCode: String?,
//        @RequestParam(defaultValue = Constant.System.PAGE_SIZE_STR) limit: Int
//    ): Result<Any> {
//        return Result.success(userService.suggest(name, roleCode, limit))
//    }
//
    @PostMapping("/login")
    fun login(@RequestBody vo: UserVo): Result<Any> {
        return Result.success(userService.login(vo))
    }

    //
    @GetMapping("/logout")
    fun logout(): Result<Any> {
        return Result.success(userService.logout())
    }

    //
//    @PostMapping("/insert")
//    fun insert(@RequestBody map: Map<String, Any>): Result<Any> {
//        return Result.success(userService.insert(map))
//    }
//
    @PostMapping("/register")
    fun register(@RequestBody map: Map<String, Any>): Result<Any> {
        return Result.success(userService.insert(map))
    }

    /***
     * 根据用户id修改用户信息(包括密码)
     */
    @PostMapping("/update")
    fun update(@RequestBody map: Map<String, Any>): Result<Any> {
        return Result.success(userService.update(map))
    }

    /***
     * 根据原密码修改当前登录用户的密码
     */
    @PostMapping("/update/password")
    fun updatePassword(@RequestBody vo: UserVo): Result<Any> {
        return Result.success(userService.updatePassword(vo))
    }

    /***
     * 获取找回方式/根据用户名发送验证码/找回密码
     * 找回密码
     */
    @PostMapping("/find/password")
    fun findPassword(@RequestBody vo: UserVo): Result<Any> {
        return Result.success(userService.findPassword(vo))
    }

    @DeleteMapping("/delete")
    fun delete(@RequestBody ids: List<String>): Result<Any> {
        return Result.success(userService.delete(ids, 0))
    }

    /***
     * 物理删除
     */
    @DeleteMapping("/delete/phy")
    fun phyDelete(@RequestBody ids: List<String>): Result<Any> {
        return Result.success(userService.delete(ids, 1))
    }
//
//    @GetMapping("/list/role/{ids}")
//    fun listRoleByIds(@PathVariable ids: List<Long>): Result<Any> {
//        return Result.success(userService.listRoleByIds(ids))
//    }
}
