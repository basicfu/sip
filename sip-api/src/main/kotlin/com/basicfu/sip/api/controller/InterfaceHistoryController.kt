package com.basicfu.sip.api.controller

import com.basicfu.sip.api.model.vo.InterfaceHistoryVo
import com.basicfu.sip.api.service.InterfaceHistoryService
import com.basicfu.sip.core.model.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author basicfu
 * @date 2019/1/18
 */
@RestController
@RequestMapping("/interface-history")
class InterfaceHistoryController {
    @Autowired
    lateinit var interfaceHistoryService: InterfaceHistoryService

    @GetMapping("/project/{id}")
    fun listByProjectId(@PathVariable id: Long, vo: InterfaceHistoryVo): Result<Any> {
        return Result.success(interfaceHistoryService.listByProjectId(id, vo))
    }

    @GetMapping("/interface/{id}")
    fun listByInterfaceId(@PathVariable id: Long, vo: InterfaceHistoryVo): Result<Any> {
        return Result.success(interfaceHistoryService.listByInterfaceId(id, vo))
    }
}
