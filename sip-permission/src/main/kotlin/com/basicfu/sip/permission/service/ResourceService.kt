package com.basicfu.sip.permission.service

import com.basicfu.sip.core.common.exception.CustomException
import com.basicfu.sip.core.common.mapper.example
import com.basicfu.sip.core.common.mapper.generate
import com.basicfu.sip.core.service.BaseService
import com.basicfu.sip.permission.common.Enum
import com.basicfu.sip.permission.mapper.ResourceMapper
import com.basicfu.sip.permission.model.dto.ResourceDto
import com.basicfu.sip.core.model.po.Resource
import com.basicfu.sip.permission.model.vo.ResourceVo
import com.github.pagehelper.PageInfo
import org.springframework.stereotype.Service

/**
 * @author basicfu
 * @date 2018/7/9
 */
@Service
class ResourceService : BaseService<ResourceMapper, Resource>() {

    fun list(vo: ResourceVo): PageInfo<ResourceDto> {
        return selectPage(example<Resource> {
            andLike {
                name = vo.keyword
                url = vo.keyword
            }
        })
    }

    fun all(): List<ResourceDto> {
        return to(mapper.selectAll())
    }

    fun insert(vo: ResourceVo): Int {
        if (mapper.selectCount(generate {
                serviceId = vo.serviceId
                url = vo.url
                method = vo.method
            }) != 0) throw CustomException(Enum.Resource.SERVICE_URL_METHOD_UNIQUE)
        val po = dealInsert(to<Resource>(vo))
        return mapper.insertSelective(po)
    }

    fun update(vo: ResourceVo): Int {
        val checkUrl = mapper.selectOne(generate {
            serviceId = vo.serviceId
            url = vo.url
            method = vo.method
        })
        if (checkUrl != null && checkUrl.id != vo.id) throw CustomException(Enum.Resource.SERVICE_URL_METHOD_UNIQUE)
        val po = dealUpdate(to<Resource>(vo))
        return mapper.updateByPrimaryKeySelective(po)
    }

    fun delete(ids: List<Long>): Int {
        return deleteByIds(ids)
    }
}
