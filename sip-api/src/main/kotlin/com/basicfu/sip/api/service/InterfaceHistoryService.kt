package com.basicfu.sip.api.service

import com.basicfu.sip.api.mapper.InterfaceHistoryMapper
import com.basicfu.sip.api.model.dto.InterfaceHistoryDto
import com.basicfu.sip.api.model.po.InterfaceHistory
import com.basicfu.sip.api.model.vo.InterfaceHistoryVo
import com.basicfu.sip.core.common.mapper.example
import com.basicfu.sip.core.service.BaseService
import com.github.pagehelper.PageInfo
import org.springframework.stereotype.Service

/**
 * @author basicfu
 * @date 2019/1/18
 */
@Service
class InterfaceHistoryService : BaseService<InterfaceHistoryMapper, InterfaceHistory>() {
    fun listByProjectId(id:Long,vo:InterfaceHistoryVo): PageInfo<InterfaceHistoryDto> {
        return selectPage(example<InterfaceHistory> {
            andEqualTo {
                projectId=id
                type=vo.type
            }
            orderByDesc(InterfaceHistory::cdate)
        })
    }

    fun listByInterfaceId(id: Long,vo:InterfaceHistoryVo): PageInfo<InterfaceHistoryDto> {
        return selectPage(example<InterfaceHistory> {
            andEqualTo {
                interfaceId = id
                type=vo.type
            }
            orderByDesc(InterfaceHistory::cdate)
        })
    }
}
