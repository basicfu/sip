package com.basicfu.sip.base.service

import com.basicfu.sip.base.common.enum.Enum
import com.basicfu.sip.base.mapper.AppMapper
import com.basicfu.sip.base.mapper.PermissionMapper
import com.basicfu.sip.base.mapper.PermissionResourceMapper
import com.basicfu.sip.base.mapper.ResourceMapper
import com.basicfu.sip.base.model.dto.PermissionDto
import com.basicfu.sip.base.model.po.Permission
import com.basicfu.sip.base.model.po.PermissionResource
import com.basicfu.sip.base.model.po.Resource
import com.basicfu.sip.base.model.vo.PermissionVo
import com.basicfu.sip.base.util.AppUtil
import com.basicfu.sip.core.common.exception.CustomException
import com.basicfu.sip.core.common.mapper.example
import com.basicfu.sip.core.common.mapper.generate
import com.basicfu.sip.core.service.BaseService
import com.github.pagehelper.PageInfo
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.yaml.snakeyaml.Yaml

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
    @Autowired
    lateinit var appMapper: AppMapper
    @Autowired
    lateinit var roleService: RoleService

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

//    /**
//     * 查询permissionResource,resource自己的appId和sip的，appNotCheck目前无法满足，分页时有count语句
//     */
//    fun listResourceById(id: Long, q: String?): PageInfo<ResourceDto> {
//        val likeValue = SqlUtil.dealLikeValue(q)
//        startPage()
//        val appIds = arrayListOf(AppUtil.getAppId())
//        val appCode = AppUtil.getAppCode()
//        if (Constant.System.APP_SYSTEM_CODE != appCode) {
//            appIds.add(AppUtil.getAppIdByAppCode(Constant.System.APP_SYSTEM_CODE))
//        }
//        var sql =
//            "select r.id as id,service_id as serviceId,url,method,name,pr.cdate as cdate from permission_resource pr " +
//                    "LEFT JOIN resource r on pr.resource_id=r.id WHERE pr.permission_id=$id " +
//                    "and pr.app_id in (${StringUtils.join(appIds, ",")}) and r.app_id in (${StringUtils.join(
//                        appIds,
//                        ","
//                    )})"
//        likeValue?.let { sql += " and (r.url like $likeValue or r.name like $likeValue)" }
//        sql += " ORDER BY pr.cdate DESC"
//        AppUtil.notCheckApp(2)
//        val result = resourceMapper.selectBySql(sql)
//        return getPageInfo(result)
//    }

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

    /**
     * 允许添加自己和sip的资源
     */
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
        ids.forEach {
            val pr = PermissionResource()
            pr.permissionId = vo.id
            pr.resourceId = it
            permissionResources.add(dealInsert(pr))
        }
        val count = permissionResourceMapper.insertList(permissionResources)
        roleService.refreshRolePermission()
        return count
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
        val count = deleteByIds(ids)
        roleService.refreshRolePermission()
        return count
    }

    fun deleteResource(vo: PermissionVo): Int {
        val count = permissionResourceMapper.deleteByExample(example<PermissionResource> {
            andEqualTo(PermissionResource::permissionId, vo.id)
            andIn(PermissionResource::resourceId, vo.resourceIds!!)
        })
        roleService.refreshRolePermission()
        return count
    }

    /**
     * 如果已经存在permission不在此添加，判断下的资源是否有新增，只处理新增
     */
    @Suppress("UNCHECKED_CAST")
    fun import(vo: PermissionVo) {
        val yaml = Yaml().load(vo.value) as LinkedHashMap<String, Any>
        val permissionsStr = yaml["permission"] as ArrayList<Any>
        val permissionMap = mapper.select(generate {
            appId=vo.appId
        }).associateBy { "${it.name}|${it.code}" }
        val resourceList = resourceMapper.selectAll()
        val appMap = appMapper.selectAll().associateBy({ it.id },{it.code})
        val resourceMap = resourceList.associateBy { it.id!! }
//        val resourceUrlMap = resourceList.associateBy { "${it?.url}|${it?.method}|${appServiceMap[it?.serviceId]}" }
        val resourceUrlMap = resourceList.associateBy { "${it?.url}|${it?.method}|${appMap[it?.appId]}" }
        val permissions = arrayListOf<Permission>()
        val insertPermissions = arrayListOf<Permission>()
        val insertPermissionResources = arrayListOf<PermissionResource>()
        permissionsStr.forEach {
            if (it is String) {
                val permissionArray = it.split("|")
                if (permissionArray.size == 2) {
                    val permission = permissionMap[it]
                    if (permission == null) {
                        insertPermissions.add(dealInsert(generate {
                            appId=vo.appId
                            name = permissionArray[0]
                            code = permissionArray[1]
                        }))
                    } else {
                        permissions.add(permission)
                    }
                }
            } else {
                val resources = it as LinkedHashMap<String, ArrayList<String>>
                resources.forEach { k, _ ->
                    val permissionArray = k.split("|")
                    if (permissionArray.size == 2) {
                        val permission = permissionMap[k]
                        if (permission == null) {
                            insertPermissions.add(dealInsert(generate {
                                appId=vo.appId
                                name = permissionArray[0]
                                code = permissionArray[1]
                            }))
                        } else {
                            permissions.add(permission)
                        }
                    }
                }
            }
        }
        if (insertPermissions.isNotEmpty()) {
            mapper.insertList(insertPermissions)
            permissions.addAll(insertPermissions)
        }
        val ids = permissions.map { it.id!! }
        val permissionResources = permissionResourceMapper.selectByExample(example<PermissionResource> {
            andIn(PermissionResource::permissionId, ids)
        }).groupBy({ it.permissionId }, { resourceMap[it.resourceId] })
        val permissionFullMap = permissions.associateBy { "${it.name}|${it.code}" }
        permissionsStr.forEach {
            if (it !is String) {
                val resources = it as LinkedHashMap<String, ArrayList<String>>
                resources.forEach { k, v ->
                    val permissionArray = k.split("|")
                    if (permissionArray.size == 2) {
                        val permission = permissionFullMap[k]!!
                        val permissionResourceMap =
                            permissionResources[permission.id]?.associateBy({ "${it?.url}|${it?.method}|${appMap[it?.appId]}" },
                                { it }) ?: hashMapOf()
                        v.forEach {
                            val resource = permissionResourceMap[it]
                            if (resource == null) {
                                resourceUrlMap[it]?.let {
                                    insertPermissionResources.add(dealInsert(generate {
                                        permissionId = permission.id
                                        resourceId = it.id
                                    }))
                                }
                            }
                        }
                    }
                }
            }
        }
        if (insertPermissionResources.isNotEmpty()) {
            permissionResourceMapper.insertList(insertPermissionResources)
        }
        roleService.refreshRolePermission()
    }

    fun export(vo: PermissionVo): String {
        val permissions = mapper.select(generate {
            appId=vo.appId
        })
        val ids = permissions.map { it.id!! }
        val permissionResources = permissionResourceMapper.selectByExample(example<PermissionResource> {
            andIn(PermissionResource::permissionId, ids)
        })
        val resourceIds = permissionResources.map { it.resourceId }
        val permissionResourceMap = permissionResources.groupBy { it.permissionId!! }
        AppUtil.notCheckApp()
        val resourceMap = resourceMapper.selectAll().associateBy { it.id!! }
        val appMap = appMapper.selectAll().associateBy({ it.id },{it.code})
        val prefix = "permission:\r\n"
        val listStr = permissions.map { permission ->
            var sb = "  - ${permission.name}|${permission.code}"
            val list = permissionResourceMap[permission.id]
            if (list != null) {
                sb += ":\r\n" + StringUtils.join(
                    list.map { resourceMap[it.resourceId]?.let { "    - ${it.url}|${it.method}|${appMap[it.appId]}" } }.filter { it != null },
                    "\r\n"
                )
            }
            sb
        }
        return prefix + StringUtils.join(listStr, "\r\n")
    }
}
