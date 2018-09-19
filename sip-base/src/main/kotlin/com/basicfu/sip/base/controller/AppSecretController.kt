package com.basicfu.sip.base.controller

import com.basicfu.sip.base.model.vo.AppSecretVo
import com.basicfu.sip.base.service.AppSecretService
import com.basicfu.sip.core.annotation.Insert
import com.basicfu.sip.core.annotation.Update
import com.basicfu.sip.core.model.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

/**
 * @author basicfu
 * @date 2018/9/5
 */
@RestController
@RequestMapping("/app-secret")
class AppSecretController {
    @Autowired
    lateinit var appSecretService: AppSecretService

    @GetMapping("/list")
    fun list(vo: AppSecretVo): Result<Any> {
        return Result.success(appSecretService.list(vo))
    }

    @GetMapping("/all")
    fun all(): Result<Any> {
        return Result.success(appSecretService.all())
    }

    @PostMapping("/insert")
    fun insert(@Validated(Insert::class) @RequestBody vo: AppSecretVo): Result<Any> {
        return Result.success(appSecretService.insert(vo))
    }

    @PostMapping("/update")
    fun update(@Validated(Update::class) @RequestBody vo: AppSecretVo): Result<Any> {
        return Result.success(appSecretService.update(vo))
    }

    @DeleteMapping("/delete")
    fun delete(@RequestBody ids: List<Long>): Result<Any> {
        return Result.success(appSecretService.delete(ids))
    }
}
