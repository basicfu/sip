package com.basicfu.sip.api.service

import com.basicfu.sip.api.mapper.InterfaceMapper
import com.basicfu.sip.api.mapper.ProjectCategoryMapper
import com.basicfu.sip.api.model.dto.ProjectCategoryDto
import com.basicfu.sip.api.model.po.Interface
import com.basicfu.sip.api.model.po.ProjectCategory
import com.basicfu.sip.api.model.vo.ProjectCategoryVo
import com.basicfu.sip.common.enum.Enum
import com.basicfu.sip.core.common.exception.CustomException
import com.basicfu.sip.core.common.mapper.example
import com.basicfu.sip.core.common.mapper.generate
import com.basicfu.sip.core.service.BaseService
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author basicfu
 * @date 2019/1/11
 */
@Service
class ProjectCategoryService : BaseService<ProjectCategoryMapper, ProjectCategory>() {
    @Autowired
    lateinit var interfaceMapper: InterfaceMapper

    fun get(vo: ProjectCategoryVo): List<ProjectCategoryDto> {
        val projectCategorys = mapper.selectByExample(example<ProjectCategory> {
            andEqualTo {
                projectId = vo.projectId
            }
        })
        val interfaces = interfaceMapper.selectByExample(example<Interface> {
            andEqualTo {
                projectId = vo.projectId
            }
        })
        return recursive(null, projectCategorys, interfaces)
    }

    fun insert(vo: ProjectCategoryVo): Int {
        if (mapper.selectCount(generate {
                name = vo.name
                projectId = vo.id
                pid = vo.pid!!
            }) != 0) throw CustomException(Enum.EXIST_PROJECT_CATEGORY_NAME)
        val po = dealInsert(to<ProjectCategory>(vo))
        return mapper.insertSelective(po)
    }

    fun update(vo: ProjectCategoryVo): Int {
        val checkName = mapper.selectOne(generate {
            name = vo.name
        })
        if (checkName != null && checkName.id != vo.id) throw CustomException(Enum.EXIST_PROJECT_CATEGORY_NAME)
        val po = dealUpdate(to<ProjectCategory>(vo))
        return mapper.updateByPrimaryKeySelective(po)
    }

    fun delete(ids: List<Long>?): Int {
        return deleteByIds(ids)
    }

    /**
     * 处理分类和接口树
     */
    private fun recursive(
        parent: ProjectCategoryDto?,
        pc: List<ProjectCategory>?,
        interfaces: List<Interface>?
    ): List<ProjectCategoryDto> {
        val childList = arrayListOf<ProjectCategoryDto>()
        if (pc == null || interfaces == null) {
            return childList
        }
        if (parent == null) {
            val roots = pc.filter { it.pid == 0L }
            val interfaceRoots = interfaces.filter { it.categoryId == 0L }
            roots.forEach { it ->
                val dto = ProjectCategoryDto()
                BeanUtils.copyProperties(it, dto)
                dto.type = Enum.ProjectCategoryType.DIRECTORY.name
                recursive(dto, pc, interfaces)
                childList.add(dto)
            }
            interfaceRoots.forEach {
                val dto = ProjectCategoryDto()
                dto.id = it.id
                dto.name = it.name
                dto.method = it.method
                dto.path = it.path
                dto.type = Enum.ProjectCategoryType.INTERFACE.name
                childList.add(dto)
            }
            childList.sortBy { it.sort }
        } else {
            pc.forEach { it ->
                if (it.pid == parent.id) {
                    val dto = ProjectCategoryDto()
                    BeanUtils.copyProperties(it, dto)
                    dto.type = Enum.ProjectCategoryType.DIRECTORY.name
                    childList.add(dto)
                    recursive(dto, pc, interfaces)
                }
            }
            interfaces.filter { it.categoryId == parent.id }.forEach {
                val dto = ProjectCategoryDto()
                dto.id = it.id
                dto.name = it.name
                dto.method = it.method
                dto.path = it.path
                dto.type = Enum.ProjectCategoryType.INTERFACE.name
                childList.add(dto)
            }
            childList.sortBy { it.sort }
            parent.children = childList
        }
        return childList
    }
}
