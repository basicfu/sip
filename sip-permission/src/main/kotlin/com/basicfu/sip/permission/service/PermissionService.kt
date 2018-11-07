package com.basicfu.sip.permission.service

import com.basicfu.sip.core.common.Enum
import com.basicfu.sip.core.common.exception.CustomException
import com.basicfu.sip.core.common.mapper.example
import com.basicfu.sip.core.common.mapper.generate
import com.basicfu.sip.core.service.BaseService
import com.basicfu.sip.permission.mapper.PermissionMapper
import com.basicfu.sip.permission.mapper.PermissionResourceMapper
import com.basicfu.sip.permission.mapper.ResourceMapper
import com.basicfu.sip.permission.model.dto.PermissionDto
import com.basicfu.sip.permission.model.po.Permission
import com.basicfu.sip.permission.model.po.PermissionResource
import com.basicfu.sip.permission.model.po.Resource
import com.basicfu.sip.permission.model.vo.PermissionVo
import com.github.pagehelper.PageInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author basicfu
 * @date 2018/7/9
 */
@Service
class PermissionService : BaseService<PermissionMapper, Permission>() {
    @Autowired
    lateinit var permissionResourceMapper: PermissionResourceMapper
    @Autowired
    lateinit var resourceMapper: ResourceMapper

    fun list(vo: PermissionVo): PageInfo<PermissionDto> {
        return selectPage(example<Permission> {
            andLike {
                name = vo.q
                code = vo.q
            }
            orderByDesc(Permission::cdate)
        })
    }

    fun all(): List<PermissionDto> = to(mapper.selectAll())
    fun insert(vo: PermissionVo): Int {
        if (mapper.selectCount(generate {
                name = vo.name
            }) != 0) throw CustomException(Enum.EXIST_PERMISSION_NAME)
        if (mapper.selectCount(generate {
                code = vo.code
            }) != 0) throw CustomException(Enum.EXIST_PERMISSION_CODE)
        val po = dealInsert(to<Permission>(vo))
        return mapper.insertSelective(po)
    }

    fun insertResource(vo: PermissionVo): Int {
        val ids = vo.resourceIds!!
        if (resourceMapper.selectCountByExample(example<Resource> {
                andIn(Resource::id, ids)
            }) != ids.size) throw CustomException(Enum.NOT_FOUND_RESOURCE)
        val permissionResources = arrayListOf<PermissionResource>()
        ids.forEach { it ->
            val pr = PermissionResource()
            pr.permissionId = vo.id
            pr.resourceId = it
            permissionResources.add(pr)
        }
        return permissionResourceMapper.insertList(permissionResources)
    }

    fun update(vo: PermissionVo): Int {
        val checkPermissionName = mapper.selectOne(generate {
            name = vo.name
        })
        if (checkPermissionName != null && checkPermissionName.id != vo.id) throw CustomException(
            Enum.EXIST_PERMISSION_NAME
        )
        val checkPermissionCode = mapper.selectOne(generate {
            code = vo.code
        })
        if (checkPermissionCode != null && checkPermissionCode.id != vo.id) throw CustomException(
            Enum.EXIST_PERMISSION_CODE
        )
        val po = dealUpdate(to<Permission>(vo))
        return mapper.updateByPrimaryKeySelective(po)
    }

    fun delete(ids: List<Long>): Int {
        if (ids.isNotEmpty()) {
            permissionResourceMapper.deleteByExample(example<PermissionResource> {
                andIn(PermissionResource::permissionId, ids)
            })
        }
        return deleteByIds(ids)
    }

    fun deleteResource(vo: PermissionVo): Int {
        return permissionResourceMapper.deleteByExample(example<PermissionResource> {
            andEqualTo(PermissionResource::permissionId, vo.id)
            andIn(PermissionResource::resourceId, vo.resourceIds!!)
        })
    }
}
