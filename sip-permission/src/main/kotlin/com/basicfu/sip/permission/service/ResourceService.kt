package com.basicfu.sip.permission.service

import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.basicfu.sip.client.feign.ClientFeign
import com.basicfu.sip.core.common.Constant
import com.basicfu.sip.core.common.Enum
import com.basicfu.sip.core.common.exception.CustomException
import com.basicfu.sip.core.common.mapper.example
import com.basicfu.sip.core.common.mapper.generate
import com.basicfu.sip.core.model.Result
import com.basicfu.sip.core.model.dto.AppDto
import com.basicfu.sip.core.model.dto.AppServiceDto
import com.basicfu.sip.core.model.dto.ResourceDto
import com.basicfu.sip.core.service.BaseService
import com.basicfu.sip.core.util.AppUtil
import com.basicfu.sip.core.util.RedisUtil
import com.basicfu.sip.permission.mapper.MenuResourceMapper
import com.basicfu.sip.permission.mapper.PermissionResourceMapper
import com.basicfu.sip.permission.mapper.ResourceMapper
import com.basicfu.sip.permission.model.po.MenuResource
import com.basicfu.sip.permission.model.po.PermissionResource
import com.basicfu.sip.permission.model.po.Resource
import com.basicfu.sip.permission.model.vo.ResourceVo
import com.github.pagehelper.PageInfo
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
    lateinit var clientFeign: ClientFeign
    @Autowired
    lateinit var springClientFactory: SpringClientFactory
    @Autowired
    lateinit var permissionResourceMapper: PermissionResourceMapper
    @Autowired
    lateinit var menuResourceMapper: MenuResourceMapper

    fun list(vo: ResourceVo): PageInfo<ResourceDto> {
        return selectPage(example<Resource> {
            andEqualTo(Resource::serviceId, vo.serviceId)
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

    @Suppress("UNCHECKED_CAST")
    fun sync(vo: ResourceVo): Result<Any> {
        val app = RedisUtil.hGet<AppDto>(Constant.Redis.APP, AppUtil.getAppCode()!!)!!
        val syncList = arrayListOf<AppServiceDto>()
        val existResources = arrayListOf<Resource>()
        if (vo.serviceId == null) {
            syncList.addAll(app.services!!)
            existResources.addAll(mapper.selectAll())
        } else {
            val serviceMap = app.services!!.associateBy { it.id }
            syncList.add(serviceMap[vo.serviceId]!!)
            existResources.addAll(mapper.select(generate {
                serviceId = vo.serviceId
            }))
        }
        val serviceResource = existResources.groupBy { it.serviceId }
        val insertResource = arrayListOf<Resource>()
        val deleteIds = arrayListOf<Long>()
        val result = JSONArray()
        syncList.forEach { service ->
            val serviceTag = if (service.serverId != null) {
                service.serverId.toString()
            } else {
                service.url!!
            }
            // TODO url的形式暂未考虑
            val loadBalancer = springClientFactory.getLoadBalancer(service.serverId)
            var insertCount = 0
            var deleteCount = 0
            val insertDetail = arrayListOf<ResourceDto>()
            val deleteDetail = arrayListOf<ResourceDto>()
            var available = false
            if (loadBalancer != null && loadBalancer.reachableServers.isNotEmpty()) {
                try {
                    val array = clientFeign.sipClientUrl(serviceTag).data
                    available = true
                    val resourceMap =serviceResource[service.id]?.associateBy { it.url + it.method }?.toMutableMap() ?: hashMapOf()
                    array?.forEach {
                        val obj = it as LinkedHashMap<String, Any>
                        val urls = obj["url"] as ArrayList<String>
                        val methods = obj["requestMethod"] as ArrayList<String>
                        urls.forEach { url ->
                            methods.forEach { method ->
                                val resource = resourceMap[url + method]
                                if (resource == null) {
                                    val po = generate<Resource> {
                                        serviceId = service.id
                                        this.url = url
                                        this.method = method
                                        cdate = (java.lang.System.currentTimeMillis() / 1000).toInt()
                                        udate = cdate
                                        name = ""
                                    }
                                    insertResource.add(po)
                                    insertCount++
                                    insertDetail.add(to<ResourceDto>(po)!!)
                                }
                                resourceMap.remove(url + method)
                            }
                        }
                    }
                    val ids = resourceMap.values.map { it.id!! }
                    deleteCount = ids.size
                    deleteDetail.addAll(to(resourceMap.values.toList()))
                    deleteIds.addAll(ids)
                }catch (e: Exception){
                    available = false
                }
            }
            val item = JSONObject()
            item["name"] = service.name
            item["serviceId"] = service.id
            item["available"] = available
            item["insertCount"] = insertCount
            item["deleteCount"] = deleteCount
            item["insertDetail"] = insertDetail
            item["deleteDetail"] = deleteDetail
            result.add(item)
        }
        return if (vo.type == 2) {
            if (insertResource.isNotEmpty()) {
                mapper.insertList(insertResource)
            }
            if (deleteIds.isNotEmpty()) {
                delete(deleteIds)
            }
            Result.success(null, "操作成功")
        } else {
            Result.success(result)
        }
    }

    fun insert(vo: ResourceVo): Int {
        if (mapper.selectCount(generate {
                serviceId = vo.serviceId
                url = vo.url
                method = vo.method
            }) != 0) throw CustomException(Enum.SERVICE_URL_METHOD_UNIQUE)
        val po = dealInsert(to<Resource>(vo))
        return mapper.insertSelective(po)
    }

    fun update(vo: ResourceVo): Int {
        val checkUrl = mapper.selectOne(generate {
            serviceId = vo.serviceId
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
        return deleteByIds(ids)
    }
}
