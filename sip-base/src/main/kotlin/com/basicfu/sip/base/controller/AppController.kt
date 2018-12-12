package com.basicfu.sip.base.controller

import com.basicfu.sip.base.model.vo.AppVo
import com.basicfu.sip.base.service.AppService
import com.basicfu.sip.core.model.Result
import com.basicfu.sip.core.annotation.Insert
import com.basicfu.sip.core.annotation.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

/**
 * @author basicfu
 * @date 2018/7/12
 */
@RestController
@RequestMapping("/app")
class AppController {
    @Autowired
    lateinit var appService: AppService

    @GetMapping("/list")
    fun list(vo: AppVo): Result<Any> {
        return Result.success(appService.list(vo))
    }

    @GetMapping("/all")
    fun all(): Result<Any> {
        return Result.success(appService.all())
    }

    @PostMapping("/insert")
    fun insert(@Validated(Insert::class) @RequestBody vo: AppVo): Result<Any> {
        return Result.success(appService.insert(vo))
    }

    @PostMapping("/update")
    fun update(@Validated(Update::class) @RequestBody vo: AppVo): Result<Any> {
        return Result.success(appService.update(vo))
    }

    @DeleteMapping("/delete")
    fun delete(@RequestBody ids: List<Long>): Result<Any> {
        return Result.success(appService.delete(ids))
    }
}
