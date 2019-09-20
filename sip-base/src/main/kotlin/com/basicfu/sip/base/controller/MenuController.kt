package com.basicfu.sip.base.controller

import com.basicfu.sip.base.model.vo.MenuVo
import com.basicfu.sip.base.service.MenuService
import com.basicfu.sip.core.model.Result
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
    fun all(appId: Long?): Result<Any> {
        return Result.success(menuService.all(appId))
    }

//    @GetMapping("/list/{id}/resource")
//    fun listResourceById(@PathVariable id: Long, @RequestParam(required = false) q: String?): Result<Any> {
//        return Result.success(menuService.listResourceById(id, q))
//    }

    @PostMapping("/insert")
    fun insert(@RequestBody vo: MenuVo): Result<Any> {
        return Result.success(menuService.insert(vo))
    }

    @PostMapping("/insert/resource")
    fun insertResource(@RequestBody vo: MenuVo): Result<Any> {
        return Result.success(menuService.insertResource(vo))
    }

    @PostMapping("/update")
    fun update(@RequestBody vo: MenuVo): Result<Any> {
        return Result.success(menuService.update(vo))
    }

    @PostMapping("/update/{id}/{display}")
    fun updateDisplay(@PathVariable("id") id: Long, @PathVariable("display") display: Boolean): Result<Any> {
        return Result.success(menuService.updateDisplay(id, display))
    }

    @PostMapping("/update/sort/{dragId}/{hoverId}")
    fun updateSort(@PathVariable("dragId") dragId: Long, @PathVariable("hoverId") hoverId: Long): Result<Any> {
        return Result.success(menuService.updateSort(dragId, hoverId))
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
