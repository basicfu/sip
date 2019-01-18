package com.basicfu.sip.api.controller

import com.basicfu.sip.api.model.vo.ProjectVo
import com.basicfu.sip.api.service.ProjectService
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
@RequestMapping("/project")
class ProjectController {
    @Autowired
    lateinit var projectService: ProjectService

    @GetMapping("/all")
    fun all(): Result<Any> {
        return Result.success(projectService.all())
    }

    @PostMapping("/insert")
    fun insert(@Validated(Insert::class) @RequestBody vo: ProjectVo): Result<Any> {
        return Result.success(projectService.insert(vo))
    }

    @PostMapping("/update")
    fun update(@Validated(Update::class) @RequestBody vo: ProjectVo): Result<Any> {
        return Result.success(projectService.update(vo))
    }

    @DeleteMapping("/delete")
    fun delete(@RequestBody ids: List<Long>): Result<Any> {
        return Result.success(projectService.delete(ids))
    }

}
