package com.basicfu.sip.permission.controller

import com.basicfu.sip.core.model.Result
import com.basicfu.sip.permission.model.vo.RoleVo
import com.basicfu.sip.permission.service.RoleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * @author basicfu
 * @date 2018/7/9
 */
@RestController
@RequestMapping("/role")
class RoleController {
    @Autowired
    lateinit var roleService: RoleService

    @GetMapping("/get/permission/{uid}")
    fun list(@PathVariable uid: Long): Result<Any> {
        return Result(roleService.getPermissionByUid(uid))
    }

    @GetMapping("/all")
    fun all(): Result<Any> {
        return Result(roleService.all())
    }

    @PostMapping("/insert")
    fun insert(@RequestBody vo: RoleVo): Result<Any> {
        return Result(roleService.insert(vo))
    }

    @PostMapping("/insert/user")
    fun insertUser(@RequestBody vo: RoleVo): Result<Any> {
        return Result(roleService.insertUser(vo))
    }

    @PostMapping("/insert/menu")
    fun insertMenu(@RequestBody vo: RoleVo): Result<Any> {
        return Result(roleService.insertMenu(vo))
    }

    @PostMapping("/insert/permission")
    fun insertPermission(@RequestBody vo: RoleVo): Result<Any> {
        return Result(roleService.insertPermission(vo))
    }

    @PostMapping("/update")
    fun update(@RequestBody vo: RoleVo): Result<Any> {
        return Result(roleService.update(vo))
    }

    @DeleteMapping("/delete")
    fun delete(ids: List<Long>): Result<Any> {
        return Result(roleService.delete(ids))
    }

    @DeleteMapping("/delete/menu")
    fun deleteMenu(vo: RoleVo): Result<Any> {
        return Result(roleService.deleteMenu(vo))
    }

    @DeleteMapping("/delete/permission")
    fun deletePermission(vo: RoleVo): Result<Any> {
        return Result(roleService.deletePermission(vo))
    }
}
