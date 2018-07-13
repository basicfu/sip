package com.basicfu.sip.dict.controller

import com.basicfu.sip.core.model.Result
import com.basicfu.sip.core.model.vo.DictVo
import com.basicfu.sip.dict.service.DictService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * @author basicfu
 * @date 2018/6/25
 */
@RestController
@RequestMapping("/dict")
class DictContoller {
    @Autowired
    lateinit var dictService: DictService

    @GetMapping("/all")
    fun all(): Result<Any> {
        return Result(dictService.all())
    }

    @GetMapping("/list")
    fun list(vo: DictVo): Result<Any> {
        return Result(dictService.list(vo))
    }

    @GetMapping("/get/{value}")
    fun get(@PathVariable value: String): Result<Any> {
        return Result(dictService.get(value))
    }

    @PostMapping("/insert")
    fun insert(@RequestBody vo: DictVo): Result<Any> {
        return Result(dictService.insert(vo))
    }

    @PostMapping("/update")
    fun update(@RequestBody vo: DictVo): Result<Any> {
        return Result(dictService.update(vo))
    }

    @DeleteMapping("/delete")
    fun delete(@RequestBody ids: List<Long>): Result<Any> {
        return Result(dictService.delete(ids))
    }

    @PostMapping("/import")
    fun import(@RequestBody vo: DictVo): Result<Any> {
        return Result(dictService.import(vo))
    }

    @PostMapping("/export")
    fun export(vo: DictVo): Result<Any> {
        return Result(true,dictService.export(vo))
    }
}
