package com.basicfu.sip.permission.service

import com.basicfu.sip.core.exception.CustomException
import com.basicfu.sip.core.mapper.example
import com.basicfu.sip.core.mapper.generate
import com.basicfu.sip.core.service.BaseService
import com.basicfu.sip.permission.common.Enum
import com.basicfu.sip.permission.mapper.PermissionMapper
import com.basicfu.sip.permission.mapper.PermissionResourceMapper
import com.basicfu.sip.permission.mapper.ResourceMapper
import com.basicfu.sip.permission.model.dto.PermissionDto
import com.basicfu.sip.permission.model.po.MenuResource
import com.basicfu.sip.permission.model.po.Permission
import com.basicfu.sip.permission.model.po.PermissionResource
import com.basicfu.sip.permission.model.po.Resource
import com.basicfu.sip.permission.model.vo.MenuVo
import com.basicfu.sip.permission.model.vo.PermissionVo
import com.github.pagehelper.PageInfo
import org.apache.commons.lang.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author basicfu
 * @date 2018/7/9
 */
@Service
class PermissionService : BaseService<PermissionMapper, Permission>() {
    @Autowired
    lateinit var prMapper: PermissionResourceMapper
    @Autowired
    lateinit var resourceMapper: ResourceMapper

    fun list(vo: PermissionVo): PageInfo<PermissionDto> {
        return selectPage(example<Permission> {
            andLike {
                name = vo.keyword
            }
        })
    }

    fun all(): List<PermissionDto> = to(mapper.selectAll())
    fun insert(vo: PermissionVo): Int {
        if (mapper.selectCount(generate {
                name = vo.name
            }) != 0) throw CustomException(Enum.Resource.EXIST_URL)
        val po = dealInsert(to<Permission>(vo))
        return mapper.insertSelective(po)
    }
    fun insertResource(vo: PermissionVo): Int {
        val ids = vo.resourceIds!!
        if (resourceMapper.selectCountByExample(example<Resource> {
                andIn(Resource::id, ids)
            }) != ids.size) throw CustomException(Enum.Permission.RESOURCE_NOT_FOUND)
        val permissionResources = arrayListOf<PermissionResource>()
        ids.forEach { it ->
            val pr = PermissionResource()
            pr.permissionId = vo.id
            pr.resourceId = it
            permissionResources.add(pr)
        }
        return prMapper.insertList(permissionResources)
    }
    fun update(vo: PermissionVo): Int {
        val checkPermission = mapper.selectOne(generate {
            name = vo.name
        })
        if (checkPermission != null && checkPermission.id != vo.id) throw CustomException(Enum.Permission.EXIST_NAME)
        val po = dealUpdate(to<Permission>(vo))
        return mapper.updateByPrimaryKeySelective(po)
    }

    fun delete(ids: List<Long>): Int {
        if (ids.isNotEmpty()) {
            prMapper.deleteByIds(StringUtils.join(ids, ","))
        }
        return deleteByIds(ids)
    }
}
