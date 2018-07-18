package com.basicfu.sip.base.controller

import com.basicfu.sip.base.model.vo.UserTemplateVo
import com.basicfu.sip.base.service.UserTemplateService
import com.basicfu.sip.core.model.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * @author basicfu
 * @date 2018/6/30
 */
@RestController
@RequestMapping("/user-template")
class UserTemplateController {
    @Autowired
    lateinit var userTemplateService: UserTemplateService

    @GetMapping("/list")
    fun all(vo: UserTemplateVo): Result<Any> {
        return Result.success(userTemplateService.list(vo))
    }

    @GetMapping("/all")
    fun all(): Result<Any> {
        return Result.success(userTemplateService.all())
    }

    @PostMapping("/insert")
    fun insert(@RequestBody vo: UserTemplateVo): Result<Any> {
        return Result.success(userTemplateService.insert(vo))
    }

    @PostMapping("/update")
    fun update(@RequestBody vo: UserTemplateVo): Result<Any> {
        return Result.success(userTemplateService.update(vo))
    }

    @DeleteMapping("/delete")
    fun delete(@RequestBody ids: List<Long>): Result<Any> {
        return Result.success(userTemplateService.delete(ids))
    }
}
