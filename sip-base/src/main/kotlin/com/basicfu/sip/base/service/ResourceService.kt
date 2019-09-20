package com.basicfu.sip.base.service

import com.basicfu.sip.base.common.constant.Constant
import com.basicfu.sip.base.common.enum.Enum
import com.basicfu.sip.base.mapper.MenuResourceMapper
import com.basicfu.sip.base.mapper.PermissionResourceMapper
import com.basicfu.sip.base.mapper.ResourceMapper
import com.basicfu.sip.base.model.dto.ResourceDto
import com.basicfu.sip.base.model.po.MenuResource
import com.basicfu.sip.base.model.po.PermissionResource
import com.basicfu.sip.base.model.po.Resource
import com.basicfu.sip.base.model.vo.ResourceVo
import com.basicfu.sip.base.util.AppUtil
import com.basicfu.sip.core.common.exception.CustomException
import com.basicfu.sip.core.common.mapper.example
import com.basicfu.sip.core.common.mapper.generate
import com.basicfu.sip.core.model.Result
import com.basicfu.sip.core.service.BaseService
import com.github.pagehelper.PageInfo
import org.apache.ibatis.session.RowBounds
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.netflix.ribbon.SpringClientFactory
import org.springframework.stereotype.Service

/**
 * @author basicfu
 * @date 2018/7/9
 */
@Service
class ResourceService : BaseService<ResourceMapper, Resource>() {
    @Autowired
    lateinit var springClientFactory: SpringClientFactory
    @Autowired
    lateinit var permissionResourceMapper: PermissionResourceMapper
    @Autowired
    lateinit var menuResourceMapper: MenuResourceMapper
    @Autowired
    lateinit var roleService: RoleService

    fun list(vo: ResourceVo): PageInfo<ResourceDto> {
        return selectPage(example<Resource> {
            andEqualTo(Resource::appId, vo.appId)
            where {
                orLike {
                    name = vo.q
                    url = vo.q
                }
            }
            orderByDesc(Resource::id)
        })
    }

    fun all(): List<ResourceDto> {
        return to(mapper.selectAll())
    }

    fun suggest(q: String, limit: Int): List<ResourceDto> {
        return to(mapper.selectByExampleAndRowBounds(example<Resource> {
            where {
                orLike {
                    name = q
                    url = q
                }
            }
        }, RowBounds(0, limit)))
    }

    @Suppress("UNCHECKED_CAST")
    fun sync(vo: ResourceVo): Result<Any> {
        return Result.success("")
//        val app = RedisUtil.hGet<AppDto>(Constant.Redis.APP, AppUtil.getAppCode()!!)!!
//        val syncList = arrayListOf<AppServiceDto>()
//        val existResources = arrayListOf<Resource>()
//        if (vo.serviceId == null) {
//            syncList.addAll(app.services!!)
//            existResources.addAll(mapper.selectAll())
//        } else {
//            val serviceMap = app.services!!.associateBy { it.id }
//            syncList.add(serviceMap[vo.serviceId]!!)
//            existResources.addAll(mapper.select(generate {
//                serviceId = vo.serviceId
//            }))
//        }
//        val serviceResource = existResources.groupBy { it.serviceId }
//        val insertResource = arrayListOf<Resource>()
//        val deleteIds = arrayListOf<Long>()
//        val result = JSONArray()
//        syncList.forEach { service ->
//            val serviceTag = if (service.serverId != null) {
//                service.serverId.toString()
//            } else {
//                service.url!!
//            }
//            // TODO url的形式暂未考虑
//            val loadBalancer = springClientFactory.getLoadBalancer(service.serverId)
//            var insertCount = 0
//            var deleteCount = 0
//            val insertDetail = arrayListOf<ResourceDto>()
//            val deleteDetail = arrayListOf<ResourceDto>()
//            var available = false
////            if (loadBalancer != null && loadBalancer.reachableServers.isNotEmpty()) {
////                try {
////                    val array = clientFeign.sipClientUrl(serviceTag).data
////                    available = true
////                    val resourceMap =
////                        serviceResource[service.id]?.associateBy { it.url + it.method }?.toMutableMap() ?: hashMapOf()
////                    array?.forEach {
////                        val obj = it as LinkedHashMap<String, Any>
////                        val urls = obj["url"] as ArrayList<String>
////                        val methods = obj["requestMethod"] as ArrayList<String>
////                        urls.forEach { url ->
////                            methods.forEach { method ->
////                                val resource = resourceMap[url + method]
////                                if (resource == null) {
////                                    val po = generate<Resource> {
////                                        serviceId = service.id
////                                        this.url = url
////                                        this.method = method
////                                        cdate = (java.lang.System.currentTimeMillis() / 1000).toInt()
////                                        udate = cdate
////                                        name = ""
////                                    }
////                                    insertResource.add(po)
////                                    insertCount++
////                                    insertDetail.add(to<ResourceDto>(po)!!)
////                                }
////                                resourceMap.remove(url + method)
////                            }
////                        }
////                    }
////                    val ids = resourceMap.values.map { it.id!! }
////                    deleteCount = ids.size
////                    deleteDetail.addAll(to(resourceMap.values.toList()))
////                    deleteIds.addAll(ids)
////                } catch (e: Exception) {
////                    available = false
////                }
////            }
//            val item = JSONObject()
//            item["name"] = service.name
//            item["serviceId"] = service.id
//            item["available"] = available
//            item["insertCount"] = insertCount
//            item["deleteCount"] = deleteCount
//            item["insertDetail"] = insertDetail
//            item["deleteDetail"] = deleteDetail
//            result.add(item)
//        }
//        return if (vo.type == 2) {
//            if (insertResource.isNotEmpty()) {
//                mapper.insertList(insertResource)
//            }
//            if (deleteIds.isNotEmpty()) {
//                delete(deleteIds)
//            }
//            roleService.refreshRolePermission()
//            Result.success(null, "操作成功")
//        } else {
//            Result.success(result)
//        }
    }

    fun insert(vo: ResourceVo): Int {
        if (mapper.selectCount(generate {
                appId = vo.appId
                url = vo.url
                method = vo.method
            }) != 0) throw CustomException(Enum.SERVICE_URL_METHOD_UNIQUE)
        val po = dealInsert(to<Resource>(vo))
        return mapper.insertSelective(po)
    }

    fun update(vo: ResourceVo): Int {
        val checkUrl = mapper.selectOne(generate {
            appId = vo.appId
            url = vo.url
            method = vo.method
        })
        if (checkUrl != null && checkUrl.id != vo.id) throw CustomException(Enum.SERVICE_URL_METHOD_UNIQUE)
        val po = dealUpdate(to<Resource>(vo))
        return mapper.updateByPrimaryKeySelective(po)
    }

    /**
     * 删除资源时会删除权限/菜单-资源关系
     */
    fun delete(ids: List<Long>): Int {
        if (ids.isNotEmpty()) {
            permissionResourceMapper.deleteByExample(example<PermissionResource> {
                andIn(PermissionResource::resourceId, ids)
            })
            menuResourceMapper.deleteByExample(example<MenuResource> {
                andIn(MenuResource::resourceId, ids)
            })
        }
        val count = deleteByIds(ids)
        roleService.refreshRolePermission()
        return count
    }
}
