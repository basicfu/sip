package com.basicfu.sip.dict.controller

import com.basicfu.sip.core.model.Result
import com.basicfu.sip.core.model.vo.DictVo
import com.basicfu.sip.dict.service.DictService
import org.springframework.beans.factory.annotation.Autowired
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
    fun all(): Result {
        return Result.success(dictService.all())
    }

    @GetMapping("/list")
    fun list(vo: DictVo): Result {
        return Result.success(dictService.list(vo))
    }

    @GetMapping("/get/{value}")
    fun get(@PathVariable value:String): Result {
        return Result.success(dictService.get(value))
    }

    @PostMapping("/insert")
    fun insert(@RequestBody vo: DictVo): Result {
        dictService.insert(vo)
        return Result.insert
    }

        @PostMapping("/update")
    fun update(@RequestBody vo: DictVo): Result {
        dictService.update(vo)
        return Result.update
    }

    @DeleteMapping("/delete")
    fun delete(@RequestBody ids: List<Long>): Result {
        dictService.delete(ids)
        return Result.delete
    }
}
