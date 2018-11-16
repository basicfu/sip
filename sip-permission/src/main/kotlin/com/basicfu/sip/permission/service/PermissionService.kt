package com.basicfu.sip.permission.service

import com.basicfu.sip.common.enum.Enum
import com.basicfu.sip.common.model.dto.ResourceDto
import com.basicfu.sip.core.common.exception.CustomException
import com.basicfu.sip.core.common.mapper.example
import com.basicfu.sip.core.common.mapper.generate
import com.basicfu.sip.core.service.BaseService
import com.basicfu.sip.core.util.SqlUtil
import com.basicfu.sip.permission.mapper.PermissionMapper
import com.basicfu.sip.permission.mapper.PermissionResourceMapper
import com.basicfu.sip.permission.mapper.ResourceMapper
import com.basicfu.sip.common.model.dto.PermissionDto
import com.basicfu.sip.permission.model.po.Permission
import com.basicfu.sip.common.model.po.PermissionResource
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
        val pageInfo = selectPage<PermissionDto>(example<Permission> {
            andLike {
                name = vo.q
                code = vo.q
            }
            orderByDesc(Permission::cdate)
        })
        val ids = pageInfo.list.map { it.id!! }
        if (ids.isNotEmpty()) {
            val permissionResourceMap = permissionResourceMapper.selectByExample(example<PermissionResource> {
                select(PermissionResource::permissionId, PermissionResource::resourceId)
                andIn(PermissionResource::permissionId, ids)
            }).groupBy { it.permissionId }
            pageInfo.list.forEach {
                it.resourceCount = permissionResourceMap[it.id!!]?.size?.toLong() ?: 0
            }
        }
        return pageInfo
    }

    fun listResourceById(id: Long, q: String?): PageInfo<ResourceDto> {
        val likeValue = SqlUtil.dealLikeValue(q)
        startPage()
        var sql =
            "select r.id as id,service_id as serviceId,url,method,name,pr.cdate as cdate from permission_resource pr LEFT JOIN resource r on pr.resource_id=r.id WHERE pr.permission_id=$id"
        likeValue?.let { sql += " and (r.url like $likeValue or r.name like $likeValue)" }
        sql += " ORDER BY pr.cdate DESC"
        val result = resourceMapper.selectBySql(sql)
        return getPageInfo(result)
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
        var ids = vo.resourceIds!!
        if (resourceMapper.selectCountByExample(example<Resource> {
                andIn(Resource::id, ids)
            }) != ids.size) throw CustomException(Enum.NOT_FOUND_RESOURCE)
        val existsResourceIds = permissionResourceMapper.selectByExample(example<PermissionResource> {
            andEqualTo(PermissionResource::permissionId, vo.id)
            andIn(PermissionResource::resourceId, ids)
        }).map { it.resourceId }
        ids = ids.filter { !existsResourceIds.contains(it) }
        if (ids.isEmpty()) {
            throw CustomException(Enum.EXIST_ADD_DATA)
        }
        val permissionResources = arrayListOf<PermissionResource>()
        ids.forEach { it ->
            val pr = PermissionResource()
            pr.permissionId = vo.id
            pr.resourceId = it
            permissionResources.add(dealInsert(pr))
        }
        //TODO 处理权限  查询拥有权限的角色 拿出resourceIds加入当前
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
        //TODO 处理权限
        return deleteByIds(ids)
    }

    fun deleteResource(vo: PermissionVo): Int {
        //TODO 处理权限  查询拥有权限的角色，拿出resourceIds删除当前b   1,2,  4
        return permissionResourceMapper.deleteByExample(example<PermissionResource> {
            andEqualTo(PermissionResource::permissionId, vo.id)
            andIn(PermissionResource::resourceId, vo.resourceIds!!)
        })
    }
}
