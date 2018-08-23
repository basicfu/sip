package com.basicfu.sip.logs.controller

import com.basicfu.sip.core.model.Result
import com.basicfu.sip.logs.modal.vo.TemplateVo
import com.basicfu.sip.logs.service.TemplateService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * @author basicfu
 * @date 2018/8/23
 */
@RestController
@RequestMapping("/template")
class TemplateController {
    @Autowired
    lateinit var templateService: TemplateService

    @GetMapping("/list")
    fun list(vo: TemplateVo): Result<Any> {
        return Result.success(templateService.list(vo))
    }

    @GetMapping("/all")
    fun all(vo: TemplateVo): Result<Any> {
        return Result.success(templateService.all(vo))
    }

    @PostMapping("/insert")
    fun insert(@RequestBody vo: TemplateVo): Result<Any> {
        return Result.success(templateService.insert(vo))
    }

    @PostMapping("/insert/field")
    fun insertField(@RequestBody vo: TemplateVo): Result<Any> {
        return Result.success(templateService.insertField(vo))
    }

    @PostMapping("/update")
    fun update(@RequestBody vo: TemplateVo): Result<Any> {
        return Result.success(templateService.update(vo))
    }

    @PostMapping("/update/field")
    fun updateField(@RequestBody vo: TemplateVo): Result<Any> {
        return Result.success(templateService.updateField(vo))
    }

    @DeleteMapping("/delete")
    fun delete(ids: List<Long>): Result<Any> {
        return Result.success(templateService.delete(ids))
    }

    @DeleteMapping("/delete/field")
    fun deleteField(ids: List<Long>): Result<Any> {
        return Result.success(templateService.deleteField(ids))
    }
}
