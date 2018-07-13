package com.basicfu.sip.permission.controller

import com.basicfu.sip.core.model.Result
import com.basicfu.sip.permission.model.vo.PermissionVo
import com.basicfu.sip.permission.model.vo.RoleVo
import com.basicfu.sip.permission.service.PermissionService
import com.basicfu.sip.permission.service.ResourceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * @author basicfu
 * @date 2018/7/9
 */
@RestController
@RequestMapping("/permission")
class PermissionController {
    @Autowired
    lateinit var permissionService: PermissionService

    @GetMapping("/list")
    fun list(vo: PermissionVo): Result<Any> {
        return Result.success(permissionService.list(vo))
    }

    @GetMapping("/all")
    fun all(): Result<Any> {
        return Result.success(permissionService.all())
    }

    @PostMapping("/insert")
    fun insert(@RequestBody vo: PermissionVo): Result<Any> {
        return Result.success(permissionService.insert(vo))
    }
    @PostMapping("/insert/resource")
    fun insertPermission(@RequestBody vo: PermissionVo): Result<Any> {
        return Result.success(permissionService.insertResource(vo))
    }

    @PostMapping("/update")
    fun update(@RequestBody vo: PermissionVo): Result<Any> {
        return Result.success(permissionService.update(vo))
    }

    @DeleteMapping("/delete")
    fun delete(ids: List<Long>): Result<Any> {
        return Result.success(permissionService.delete(ids))
    }
}
