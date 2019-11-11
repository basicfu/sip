package com.basicfu.sip.base.controller

import com.basicfu.sip.base.model.vo.PermissionVo
import com.basicfu.sip.base.service.PermissionService
import com.basicfu.sip.core.model.Result
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
    fun insertResource(@RequestBody vo: PermissionVo): Result<Any> {
        return Result.success(permissionService.insertResource(vo))
    }

    @PostMapping("/update")
    fun update(@RequestBody vo: PermissionVo): Result<Any> {
        return Result.success(permissionService.update(vo))
    }

    @DeleteMapping("/delete")
    fun delete(@RequestBody ids: List<Long>): Result<Any> {
        return Result.success(permissionService.delete(ids))
    }

    @DeleteMapping("/delete/resource")
    fun deleteResource(@RequestBody vo: PermissionVo): Result<Any> {
        return Result.success(permissionService.deleteResource(vo))
    }

    @PostMapping("/import")
    fun import(vo: PermissionVo): Result<Any> {
        permissionService.import(vo)
        return Result.success(null,"导入成功")
    }

    @GetMapping("/export")
    fun export(vo: PermissionVo): Result<Any> {
        return Result.success(permissionService.export(vo))
    }
}
