package com.basicfu.sip.base.service

import com.basicfu.sip.base.common.Enum
import com.basicfu.sip.base.mapper.AppMapper
import com.basicfu.sip.base.model.dto.AppDto
import com.basicfu.sip.core.model.po.App
import com.basicfu.sip.base.model.vo.AppVo
import com.basicfu.sip.core.common.exception.CustomException
import com.basicfu.sip.core.common.mapper.example
import com.basicfu.sip.core.common.mapper.generate
import com.basicfu.sip.core.service.BaseService
import com.github.pagehelper.PageInfo
import org.springframework.stereotype.Service

/**
 * @author basicfu
 * @date 2018/7/12
 */
@Service
class AppService : BaseService<AppMapper, App>() {

    fun list(vo: AppVo): PageInfo<AppDto> {
        return selectPage(example<App> {
            andLike {
                name = vo.name
                code = vo.code
            }
        })
    }

    fun insert(vo: AppVo): Int {
        if (mapper.selectCount(generate {
                name = vo.name
            }) != 0) throw CustomException(Enum.App.NAME_EXISTS)
        if (mapper.selectCount(generate {
                code = vo.code
            }) != 0) throw CustomException(Enum.App.NAME_EXISTS)
        val po = dealInsert(to<App>(vo))
        return mapper.insertSelective(po)
    }

    fun update(vo: AppVo): Int {
        val checkName = mapper.selectOne(generate {
            name = vo.name
        })
        if (checkName != null && checkName.id != vo.id) throw CustomException(Enum.App.NAME_EXISTS)
        val checkDomain = mapper.selectOne(generate {
            code = vo.code
        })
        if (checkDomain != null && checkDomain.id != vo.id) throw CustomException(
            Enum.App.DOMAIN_EXISTS
        )
        val po = dealUpdate(to<App>(vo))
        return mapper.updateByPrimaryKeySelective(po)
    }

    fun delete(ids: List<Long>?): Int {
        return deleteByIds(ids)
    }
}
