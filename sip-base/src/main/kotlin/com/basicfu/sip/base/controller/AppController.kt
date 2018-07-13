package com.basicfu.sip.base.controller

import com.basicfu.sip.base.model.vo.AppVo
import com.basicfu.sip.base.service.AppService
import com.basicfu.sip.core.model.Result
import org.springframework.beans.factory.annotation.Autowired
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

    @PostMapping("/insert")
    fun insert(vo: AppVo): Result<Any> {
        return Result.success(appService.insert(vo))
    }

    @PostMapping("/update")
    fun update(vo: AppVo): Result<Any> {
        return Result.success(appService.update(vo))
    }

    @DeleteMapping("/delete")
    fun delete(ids: List<Long>): Result<Any> {
        return Result.success(appService.delete(ids))
    }
}
