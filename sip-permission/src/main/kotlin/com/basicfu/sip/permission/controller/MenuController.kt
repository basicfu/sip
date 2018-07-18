package com.basicfu.sip.permission.controller

import com.basicfu.sip.core.model.Result
import com.basicfu.sip.permission.model.vo.MenuVo
import com.basicfu.sip.permission.service.MenuService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * @author basicfu
 * @date 2018/7/9
 */
@RestController
@RequestMapping("/menu")
class MenuController {
    @Autowired
    lateinit var menuService: MenuService

    @GetMapping("/all")
    fun all(): Result<Any> {
        return Result.success(menuService.all())
    }

    @PostMapping("/insert")
    fun insert(@RequestBody vo: MenuVo): Result<Any> {
        return Result.success(menuService.insert(vo))
    }

    @PostMapping("/insert/resource")
    fun insertMenu(@RequestBody vo: MenuVo): Result<Any> {
        return Result.success(menuService.insertResource(vo))
    }

    @PostMapping("/update")
    fun update(@RequestBody vo: MenuVo): Result<Any> {
        return Result.success(menuService.update(vo))
    }

    @DeleteMapping("/delete")
    fun delete(@RequestBody ids: List<Long>): Result<Any> {
        return Result.success(menuService.delete(ids))
    }

    @DeleteMapping("/delete/resource")
    fun deleteMenu(@RequestBody vo: MenuVo): Result<Any> {
        return Result.success(menuService.deleteResource(vo))
    }
}
