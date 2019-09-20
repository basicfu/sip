package com.basicfu.sip.base.controller

import com.basicfu.sip.base.service.SecretService
import com.basicfu.sip.core.model.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * @author basicfu
 * @date 2018/9/5
 */
@RestController
@RequestMapping("/secret")
class SecretController {
    @Autowired
    lateinit var secretService: SecretService

    @GetMapping("/all")
    fun all(): Result<Any> {
        return Result.success(secretService.all())
    }

    @PostMapping("/insert")
    fun insert(): Result<Any> {
        return Result.success(secretService.insert())
    }

    @DeleteMapping("/delete")
    fun delete(@RequestBody ids: List<Long>): Result<Any> {
        return Result.success(secretService.delete(ids))
    }
}
