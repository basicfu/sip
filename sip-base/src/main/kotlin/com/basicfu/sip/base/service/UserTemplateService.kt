package com.basicfu.sip.base.service

import com.basicfu.sip.base.common.Enum
import com.basicfu.sip.base.mapper.UserTemplateMapper
import com.basicfu.sip.base.model.dto.UserTemplateDto
import com.basicfu.sip.base.model.po.UserTemplate
import com.basicfu.sip.base.model.vo.UserTemplateVo
import com.basicfu.sip.core.exception.CustomException
import com.basicfu.sip.core.mapper.example
import com.basicfu.sip.core.mapper.generate
import com.basicfu.sip.core.service.BaseService
import org.springframework.stereotype.Service

/**
 * @author basicfu
 * @date 2018/6/30
 */
@Service
class UserTemplateService : BaseService<UserTemplateMapper, UserTemplate>() {

    fun all(vo: UserTemplateVo): List<UserTemplateDto> {
        val sortedBy = mapper.selectByExample(example<UserTemplate> {
            andLike(UserTemplate::name, vo.name)
        }).sortedBy { it.sort }
        return to(sortedBy)
    }

    fun insert(vo: UserTemplateVo): Int {
        if (mapper.selectCount(generate {
                name = vo.name
            }) != 0) throw CustomException(Enum.UserTemplate.FIELD_NAME_EXISTS)
        val po = dealInsert(to<UserTemplate>(vo))
        return mapper.insertSelective(po)
    }

    fun update(vo: UserTemplateVo): Int {
        val po = dealUpdate(to<UserTemplate>(vo))
        return mapper.updateByPrimaryKeySelective(po)
    }

    fun delete(ids: List<Long>?): Int {
        return 0
    }
}
