package com.basicfu.sip.base.service

import com.basicfu.sip.base.common.Enum
import com.basicfu.sip.base.mapper.AppServiceMapper
import com.basicfu.sip.base.model.po.AppService
import com.basicfu.sip.base.model.vo.AppServiceVo
import com.basicfu.sip.core.common.exception.CustomException
import com.basicfu.sip.core.common.mapper.example
import com.basicfu.sip.core.common.mapper.generate
import com.basicfu.sip.core.model.dto.AppServiceDto
import com.basicfu.sip.core.service.BaseService
import com.github.pagehelper.PageInfo
import org.springframework.stereotype.Service

/**
 * @author basicfu
 * @date 2018/6/22
 */
@Service
class AppServiceService : BaseService<AppServiceMapper, AppService>() {

    fun list(vo: AppServiceVo): PageInfo<AppServiceDto> {
        return selectPage(example<AppService> {
            orLike {
                name = vo.q
                serverId = vo.q
            }
            orderByDesc(AppService::cdate)
        })
    }

    fun all(): List<AppServiceDto> {
        return to(mapper.selectAll().sortedBy { it.cdate })
    }

    fun insert(vo: AppServiceVo): Int {
        if(vo.serverId.isNullOrBlank()&&vo.url.isNullOrBlank()){
            throw CustomException(Enum.Service.SERVER_ID_OR_URL)
        }
        if (mapper.selectCount(generate {
                path = vo.path
            }) != 0) throw CustomException(Enum.Service.PATH_EXISTS)
        if (!vo.serverId.isNullOrBlank() && mapper.selectCount(generate {
                serverId = vo.serverId
            }) != 0) throw CustomException(Enum.Service.SERVER_ID_EXISTS)
        if (!vo.url.isNullOrBlank() && mapper.selectCount(generate {
                url = vo.url
            }) != 0) throw CustomException(Enum.Service.URL_EXISTS)
        val po = dealInsert(to<AppService>(vo))
        return mapper.insertSelective(po)
    }

    fun update(vo: AppServiceVo): Int {
        if(vo.serverId.isNullOrBlank()&&vo.url.isNullOrBlank()){
            throw CustomException(Enum.Service.SERVER_ID_OR_URL)
        }
        val checkPath = mapper.selectOne(generate {
            path = vo.path
        })
        if (checkPath != null && checkPath.id != vo.id) throw CustomException(Enum.Service.PATH_EXISTS)
        if (!vo.serverId.isNullOrBlank()) {
            val checkServiceId = mapper.selectOne(generate {
                serverId = vo.serverId
            })
            if (checkServiceId != null && checkServiceId.id != vo.id) throw CustomException(
                Enum.Service.SERVER_ID_EXISTS
            )
        }
        if (!vo.url.isNullOrBlank()) {
            val checkUrl = mapper.selectOne(generate {
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
