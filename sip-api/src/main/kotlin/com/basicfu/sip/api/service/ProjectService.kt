package com.basicfu.sip.api.service

import com.basicfu.sip.api.mapper.ProjectMapper
import com.basicfu.sip.api.model.dto.ProjectDto
import com.basicfu.sip.api.model.po.Project
import com.basicfu.sip.api.model.vo.ProjectVo
import com.basicfu.sip.common.enum.Enum
import com.basicfu.sip.core.common.exception.CustomException
import com.basicfu.sip.core.common.mapper.generate
import com.basicfu.sip.core.service.BaseService
import org.springframework.stereotype.Service

/**
 * @author basicfu
 * @date 2019/1/11
 */
@Service
class ProjectService : BaseService<ProjectMapper, Project>() {

    fun all(): List<ProjectDto> {
        return to(mapper.selectAll())
    }

    fun insert(vo: ProjectVo): Int {
        if (mapper.selectCount(generate {
                name = vo.name
            }) != 0) throw CustomException(Enum.EXIST_PROJECT_NAME)
        val po = dealInsert(to<Project>(vo))
        return mapper.insertSelective(po)
    }

    fun update(vo: ProjectVo): Int {
        val checkName = mapper.selectOne(generate {
            name = vo.name
        })
        if (checkName != null && checkName.id != vo.id) throw CustomException(Enum.EXIST_PROJECT_NAME)
        val po = dealUpdate(to<Project>(vo))
        return mapper.updateByPrimaryKeySelective(po)
    }

    fun delete(ids: List<Long>?): Int {
        return deleteByIds(ids)
    }
}
