package com.basicfu.sip.logs.service

import com.basicfu.sip.common.enum.Enum
import com.basicfu.sip.core.common.exception.CustomException
import com.basicfu.sip.core.common.mapper.example
import com.basicfu.sip.core.common.mapper.generate
import com.basicfu.sip.core.service.BaseService
import com.basicfu.sip.logs.mapper.TemplateFieldMapper
import com.basicfu.sip.logs.mapper.TemplateMapper
import com.basicfu.sip.logs.modal.dto.TemplateDto
import com.basicfu.sip.logs.modal.po.Template
import com.basicfu.sip.logs.modal.po.TemplateField
import com.basicfu.sip.logs.modal.vo.TemplateVo
import com.github.pagehelper.PageInfo
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author basicfu
 * @date 2018/8/23
 */
@Service
class TemplateService : BaseService<TemplateMapper, Template>() {
    @Autowired
    lateinit var templateFieldMapper: TemplateFieldMapper

    fun list(vo: TemplateVo): PageInfo<TemplateDto> {
        return selectPage(example<Template> {
            andLike(Template::name, vo.name)
        })
    }

    fun all(vo: TemplateVo): List<TemplateDto> {
        val all = mapper.selectByExample(example<Template> {
            andLike(Template::name, vo.name)
        })
        return to(all)
    }

    fun insert(vo: TemplateVo): Int {
        if (mapper.selectCount(generate {
                name = vo.name
            }) != 0) throw CustomException(Enum.EXIST_TEMPLATE_NAME)
        return mapper.insertSelective(dealInsert(to<Template>(vo)))
    }

    fun insertField(vo: TemplateVo): Int {
        val template = mapper.selectByPrimaryKey(vo.id) ?: throw CustomException(Enum.EXIST_TEMPLATE_NAME)
        val field = generate<TemplateField> {
            templateId = template.id
            name = vo.name
            enName = vo.enName
            sort = vo.sort
        }
        return templateFieldMapper.insert(dealInsert(field))
    }

    fun update(vo: TemplateVo): Int {
        val checkName = mapper.selectOne(generate {
            name = vo.name
        })
        if (checkName != null && checkName.id != vo.id) throw CustomException(
            Enum.EXIST_TEMPLATE_NAME
        )
        val po = dealUpdate(to<Template>(vo))
        return mapper.updateByPrimaryKeySelective(po)
    }

    fun updateField(vo: TemplateVo): Int {
        val field = generate<TemplateField> {
            id = vo.id
            name = vo.name
            enName = vo.enName
            sort = vo.sort
        }
        return templateFieldMapper.updateByPrimaryKeySelective(dealUpdate(field))
    }

    fun delete(ids: List<Long>): Int {
        if (ids.isNotEmpty()) {
            templateFieldMapper.deleteByExample(example<TemplateField> {
                andIn(TemplateField::templateId, ids)
            })
        }
        return deleteByIds(ids)
    }

    fun deleteField(ids: List<Long>): Int {
        return if (ids.isNotEmpty()) {
            return if (ids.size == 1) {
                templateFieldMapper.deleteByPrimaryKey(ids[0])
            } else {
                templateFieldMapper.deleteByIds(StringUtils.join(ids, ","))
            }
        } else {
            0
        }
    }
}
