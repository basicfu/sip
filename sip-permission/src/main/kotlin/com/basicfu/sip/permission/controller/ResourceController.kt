package com.basicfu.sip.permission.controller

import com.basicfu.sip.core.model.Result
import com.basicfu.sip.permission.model.vo.ResourceVo
import com.basicfu.sip.permission.service.ResourceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * @author basicfu
 * @date 2018/7/9
 */
@RestController
@RequestMapping("/resource")
class ResourceController {
    @Autowired
    lateinit var resourceService: ResourceService

    @GetMapping("/list")
    fun list(vo: ResourceVo): Result<Any> {
        return Result.success(resourceService.list(vo))
    }

    @GetMapping("/all")
    fun all(): Result<Any> {
        return Result.success(resourceService.all())
    }

    @PostMapping("/sync")
    fun sync(@RequestBody vo: ResourceVo): Result<Any> {
        return resourceService.sync(vo)
    }

    @PostMapping("/insert")
    fun insert(@RequestBody vo: ResourceVo): Result<Any> {
        return Result.success(resourceService.insert(vo))
    }

    @PostMapping("/update")
    fun update(@RequestBody vo: ResourceVo): Result<Any> {
        return Result.success(resourceService.update(vo))
    }

    @DeleteMapping("/delete")
    fun delete(@RequestBody ids: List<Long>): Result<Any> {
        return Result.success(resourceService.delete(ids))
    }
}
