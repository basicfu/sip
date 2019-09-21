package com.basicfu.sip.base.controller

import com.basicfu.sip.base.model.vo.RoleVo
import com.basicfu.sip.base.service.RoleService
import com.basicfu.sip.core.model.Result
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

    @GetMapping("/list")
    fun list(vo: RoleVo): Result<Any> {
        return Result.success(roleService.list(vo))
    }

    @GetMapping("/list/{uid}/role")
    fun listRoleByUid(@PathVariable uid: String): Result<Any> {
        return Result.success(roleService.listRoleByUid(uid))
    }

    @GetMapping("/list/{id}/user")
    fun listUserById(@PathVariable id: Long): Result<Any> {
        return Result.success(roleService.listUserById(id))
    }

    @GetMapping("/list/{id}/menu")
    fun listMenuById(@PathVariable id: Long): Result<Any> {
        return Result.success(roleService.listMenuById(id))
    }

    @GetMapping("/list/{id}/permission")
    fun listPermissionById(@PathVariable id: Long): Result<Any> {
        return Result.success(roleService.listPermissionById(id))
    }

    @GetMapping("/all")
    fun all(): Result<Any> {
        return Result.success(roleService.all())
    }

    @PostMapping("/insert")
    fun insert(@RequestBody vo: RoleVo): Result<Any> {
        return Result.success(roleService.insert(vo))
    }

    @PostMapping("/insert/user")
    fun insertUser(@RequestBody vo: RoleVo): Result<Any> {
        return Result.success(roleService.insertUser(vo))
    }

    @PostMapping("/insert/menu")
    fun insertMenu(@RequestBody vo: RoleVo): Result<Any> {
        return Result.success(roleService.insertMenu(vo))
    }

    @PostMapping("/insert/permission")
    fun insertPermission(@RequestBody vo: RoleVo): Result<Any> {
        return Result.success(roleService.insertPermission(vo))
    }

    @PostMapping("/update")
    fun update(@RequestBody vo: RoleVo): Result<Any> {
        return Result.success(roleService.update(vo))
    }

    @DeleteMapping("/delete")
    fun delete(@RequestBody ids: List<Long>): Result<Any> {
        return Result.success(roleService.delete(ids))
    }

    @DeleteMapping("/delete/user")
    fun deleteUser(@RequestBody vo: RoleVo): Result<Any> {
        return Result.success(roleService.deleteUser(vo))
    }

    @DeleteMapping("/delete/menu")
    fun deleteMenu(@RequestBody vo: RoleVo): Result<Any> {
        return Result.success(roleService.deleteMenu(vo))
    }

    @DeleteMapping("/delete/permission")
    fun deletePermission(@RequestBody vo: RoleVo): Result<Any> {
        return Result.success(roleService.deletePermission(vo))
    }
}
