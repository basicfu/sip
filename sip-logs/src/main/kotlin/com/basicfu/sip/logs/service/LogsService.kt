package com.basicfu.sip.logs.service

import com.basicfu.sip.core.service.BaseService
import com.basicfu.sip.logs.mapper.LogsMapper
import com.basicfu.sip.logs.modal.po.Logs
import com.basicfu.sip.logs.modal.vo.TemplateVo
import org.springframework.stereotype.Service

/**
 * @author basicfu
 * @date 2018/8/24
 */
@Service
class LogsService : BaseService<LogsMapper, Logs>() {

    fun list(vo: TemplateVo):String {
        println("hello")
        return "hello"
    }

}
