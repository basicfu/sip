package com.basicfu.sip.base.controller

import com.basicfu.sip.base.model.vo.UserVo
import com.basicfu.sip.base.service.UserService
import com.basicfu.sip.core.common.Constant
import com.basicfu.sip.core.model.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.ws.rs.DefaultValue

/**
 * @author basicfu
 * @date 2018/7/4
 */
@RestController
@RequestMapping("/user")
class UserController {
    @Autowired
    lateinit var userService: UserService
    @GetMapping("/get/{id}")
    fun get(@PathVariable id: Long): Result<Any> {
        return Result.success(userService.get(id))
    }
    @GetMapping
    fun getCurrentUser(): Result<Any> {
        return Result.success(userService.getCurrentUser())
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
    fun suggest(@PathVariable name:String,@RequestParam(defaultValue = Constant.System.PAGE_SIZE_STR) limit:Int): Result<Any> {
        return Result.success(userService.suggest(name,limit))
    }

    @PostMapping("/login")
    fun login(@RequestBody vo: UserVo): Result<Any> {
        return Result.success(userService.login(vo))
    }

    @GetMapping("/logout")
    fun logout(): Result<Any> {
        return Result.success(userService.logout(),Constant.System.LOGOUT)
    }

    @PostMapping("/insert")
    fun insert(@RequestBody vo: UserVo): Result<Any> {
        return Result.success(userService.insert(vo))
    }

    @PostMapping("/register")
    fun register(@RequestBody vo: UserVo): Result<Any> {
        return Result.success(userService.insert(vo))
    }

    @GetMapping("/update")
    fun update(@RequestBody vo: UserVo): Result<Any> {
        return Result.success(userService.update(vo))
    }
//
//    @PostMapping("/insert")
//    fun insert(vo: UserTemplateVo): Result<Any> {
//        return Result(userTemplateService.insert(vo))
//    }
//
//    @PostMapping("/update")
//    fun update(vo: UserTemplateVo): Result<Any> {
//        return Result(userTemplateService.update(vo))
//    }
//
//    @DeleteMapping("/delete")
//    fun delete(ids: List<Long>): Result<Any> {
//        return Result(userTemplateService.delete(ids))
//    }
}
