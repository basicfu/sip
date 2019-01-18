package com.basicfu.sip.api.controller

import com.basicfu.sip.api.model.vo.ProjectCategoryVo
import com.basicfu.sip.api.service.ProjectCategoryService
import com.basicfu.sip.core.annotation.Insert
import com.basicfu.sip.core.model.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

/**
 * @author basicfu
 * @date 2019/1/11
 */
@RestController
@RequestMapping("/project-category")
class ProjectCategoryController {
    @Autowired
    lateinit var projectCategoryService: ProjectCategoryService

    @GetMapping("/get")
    fun get(vo: ProjectCategoryVo): Result<Any> {
        return Result.success(projectCategoryService.get(vo))
    }

    @PostMapping("/insert")
    fun insert(@Validated(Insert::class) @RequestBody vo: ProjectCategoryVo): Result<Any> {
        return Result.success(projectCategoryService.insert(vo))
    }

    @PostMapping("/update")
    fun update(@Validated(Insert::class) @RequestBody vo: ProjectCategoryVo): Result<Any> {
        return Result.success(projectCategoryService.update(vo))
    }

    @DeleteMapping("/delete")
    fun delete(@RequestBody ids: List<Long>): Result<Any> {
        return Result.success(projectCategoryService.delete(ids))
    }
}
