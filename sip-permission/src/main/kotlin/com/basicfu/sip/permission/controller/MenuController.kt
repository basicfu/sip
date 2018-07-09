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
        return Result(menuService.all())
    }

    @PostMapping("/insert")
    fun insert(@RequestBody vo: MenuVo): Result<Any> {
        return Result(menuService.insert(vo))
    }

    @PostMapping("/insert/resource")
    fun insertMenu(@RequestBody vo: MenuVo): Result<Any> {
        return Result(menuService.insertResource(vo))
    }

    @PostMapping("/update")
    fun update(@RequestBody vo: MenuVo): Result<Any> {
        return Result(menuService.update(vo))
    }

    @DeleteMapping("/delete")
    fun delete(ids: List<Long>): Result<Any> {
        return Result(menuService.delete(ids))
    }

    @DeleteMapping("/delete/resource")
    fun deleteMenu(vo: MenuVo): Result<Any> {
        return Result(menuService.deleteResource(vo))
    }
}
