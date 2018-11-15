package com.basicfu.sip.logs.controller

import com.basicfu.sip.common.model.Result
import com.basicfu.sip.logs.modal.vo.TemplateVo
import com.basicfu.sip.logs.service.LogsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author basicfu
 * @date 2018/8/24
 */
@RestController
@RequestMapping("/logs")
class LogsController {
    @Autowired
    lateinit var logsService: LogsService

    @GetMapping("/list")
    fun list(vo: TemplateVo): Result<Any> {
        return Result.success(logsService.list(vo))
    }
}
