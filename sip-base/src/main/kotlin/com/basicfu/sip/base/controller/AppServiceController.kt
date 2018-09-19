package com.basicfu.sip.base.controller

import com.basicfu.sip.base.model.vo.AppServiceVo
import com.basicfu.sip.base.service.AppServiceService
import com.basicfu.sip.core.annotation.Insert
import com.basicfu.sip.core.annotation.Update
import com.basicfu.sip.core.model.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

/**
 * @author basicfu
 * @date 2018/6/22
 */
@RestController
@RequestMapping("/app-service")
class AppServiceController {
    @Autowired
    lateinit var appServiceService: AppServiceService

    @GetMapping("/list")
    fun list(vo: AppServiceVo): Result<Any> {
        return Result.success(appServiceService.list(vo))
    }

    @GetMapping("/all")
    fun all(): Result<Any> {
        return Result.success(appServiceService.all())
    }

    @PostMapping("/insert")
    fun insert(@Validated(Insert::class) @RequestBody vo: AppServiceVo): Result<Any> {
        return Result.success(appServiceService.insert(vo))
    }

    @PostMapping("/update")
    fun update(@Validated(Update::class) @RequestBody vo: AppServiceVo): Result<Any> {
        return Result.success(appServiceService.update(vo))
    }

    @DeleteMapping("/delete")
    fun delete(@RequestBody ids: List<Long>): Result<Any> {
        return Result.success(appServiceService.delete(ids))
    }
}
