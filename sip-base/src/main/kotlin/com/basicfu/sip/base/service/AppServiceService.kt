package com.basicfu.sip.base.service

import com.basicfu.sip.base.common.Enum
import com.basicfu.sip.base.mapper.AppServiceMapper
import com.basicfu.sip.base.model.vo.ServiceVo
import com.basicfu.sip.core.common.exception.CustomException
import com.basicfu.sip.core.common.mapper.generate
import com.basicfu.sip.core.model.dto.ApplicationDto
import com.basicfu.sip.core.service.BaseService
import org.springframework.stereotype.Service
import com.basicfu.sip.core.model.po.AppService

/**
 * @author basicfu
 * @date 2018/6/22
 */
@Service
class AppServiceService : BaseService<AppServiceMapper, AppService>() {

    fun all(): List<ApplicationDto> {
        return to(mapper.selectAll())
    }

    fun insert(vo: ServiceVo): Int {
        if (mapper.selectCount(generate {
                appId = vo.appId
                path = vo.path
            }) != 0) throw CustomException(Enum.Service.PATH_EXISTS)
        if (vo.serverId != null && mapper.selectCount(generate {
                appId = vo.appId
                serverId = vo.serverId
            }) != 0) throw CustomException(Enum.Service.SERVER_ID_EXISTS)
        if (vo.url != null && mapper.selectCount(generate {
                appId = vo.appId
                url = vo.url
            }) != 0) throw CustomException(Enum.Service.URL_EXISTS)
        val po = dealInsert(to<AppService>(vo))
        return mapper.insertSelective(po)
    }

    fun update(vo: ServiceVo): Int {
        val checkPath = mapper.selectOne(generate {
            appId = vo.appId
            path = vo.path
        })
        if (checkPath != null && checkPath.id != vo.id) throw CustomException(Enum.Service.PATH_EXISTS)
        if (vo.serverId != null) {
            val checkServiceId = mapper.selectOne(generate {
                appId = vo.appId
                serverId = vo.serverId
            })
            if (checkServiceId != null && checkServiceId.id != vo.id) throw CustomException(
                Enum.Service.SERVER_ID_EXISTS
            )
        }
        if (vo.url != null) {
            val checkUrl = mapper.selectOne(generate {
                appId = vo.appId
                url = vo.url
            })
            if (checkUrl != null && checkUrl.id != vo.id) throw CustomException(
                Enum.Service.URL_EXISTS
            )
        }
        val po = dealUpdate(to<AppService>(vo))
        return mapper.updateByPrimaryKeySelective(po)
    }

    fun delete(ids: List<Long>?): Int {
        return deleteByIds(ids)
    }
}
