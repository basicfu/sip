package com.basicfu.sip.base.controller

import com.basicfu.sip.base.model.vo.ServiceVo
import com.basicfu.sip.base.service.AppServiceService
import com.basicfu.sip.core.model.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * @author basicfu
 * @date 2018/6/22
 */
@RestController
@RequestMapping("/service")
class ServiceController {
    @Autowired
    lateinit var appServiceService: AppServiceService

    @GetMapping("/all")
    fun all(): Result<Any> {
        return Result.success(appServiceService.all())
    }

    @PostMapping("/insert")
    fun insert(@RequestBody vo: ServiceVo): Result<Any> {
        return Result.success(appServiceService.insert(vo))
    }

    @PostMapping("/update")
    fun update(@RequestBody vo: ServiceVo): Result<Any> {
        return Result.success(appServiceService.update(vo))
    }

    @DeleteMapping("/delete")
    fun delete(@RequestBody ids: List<Long>): Result<Any> {
        return Result.success(appServiceService.delete(ids))
    }
}
