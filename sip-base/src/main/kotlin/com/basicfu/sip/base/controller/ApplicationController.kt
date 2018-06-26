package com.basicfu.sip.base.controller

import com.basicfu.sip.base.service.ApplicationService
import com.basicfu.sip.core.model.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author basicfu
 * @date 2018/6/22
 */
@RestController
@RequestMapping("/application")
class ApplicationController {
    @Autowired
    lateinit var applicationService: ApplicationService

    @GetMapping("/all")
    fun all(): Result {
        return Result.success(applicationService.all())
    }
}