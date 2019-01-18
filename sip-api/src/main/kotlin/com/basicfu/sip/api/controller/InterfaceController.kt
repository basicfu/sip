package com.basicfu.sip.api.controller

import com.basicfu.sip.api.model.vo.InterfaceVo
import com.basicfu.sip.api.service.InterfaceService
import com.basicfu.sip.core.annotation.Insert
import com.basicfu.sip.core.annotation.Update
import com.basicfu.sip.core.model.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

/**
 * @author basicfu
 * @date 2019/1/11
 */
@RestController
@RequestMapping("/interface")
class InterfaceController {
    @Autowired
    lateinit var interfaceService: InterfaceService

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): Result<Any> {
        return Result.success(interfaceService.get(id))
    }

    @PostMapping("/insert")
    fun insert(@Validated(Insert::class) @RequestBody vo: InterfaceVo): Result<Any> {
        return Result.success(interfaceService.insert(vo))
    }

    @PostMapping("/update")
    fun update(@Validated(Update::class) @RequestBody vo: InterfaceVo): Result<Any> {
        return Result.success(interfaceService.update(vo))
    }

    @DeleteMapping("/delete")
    fun delete(@RequestBody ids: List<Long>): Result<Any> {
        return Result.success(interfaceService.delete(ids))
    }

    @PostMapping("/run/text")
    fun runText(@Validated(Insert::class) @RequestBody vo: InterfaceVo): Result<Any> {
        return Result.success(interfaceService.run(vo))
    }

    @PostMapping("/run/form")
    fun runForm(@Validated(Insert::class) vo: InterfaceVo): Result<Any> {
        return Result.success(interfaceService.run(vo))
    }
}
