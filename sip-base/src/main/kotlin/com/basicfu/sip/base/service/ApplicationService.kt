package com.basicfu.sip.base.service

import com.basicfu.sip.base.mapper.ApplicationMapper
import com.basicfu.sip.core.model.dto.ApplicationDto
import com.basicfu.sip.core.model.po.Application
import com.basicfu.sip.core.service.BaseService
import org.springframework.stereotype.Service

/**
 * @author basicfu
 * @date 2018/6/22
 */
@Service
class ApplicationService : BaseService<ApplicationMapper, Application>() {

    fun all(): List<ApplicationDto> {
        return to(mapper.selectAll())
    }
}
