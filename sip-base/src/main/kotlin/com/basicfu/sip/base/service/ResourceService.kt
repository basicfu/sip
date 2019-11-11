package com.basicfu.sip.base.service

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.basicfu.sip.base.common.enum.Enum
import com.basicfu.sip.base.mapper.MenuResourceMapper
import com.basicfu.sip.base.mapper.PermissionResourceMapper
import com.basicfu.sip.base.mapper.ResourceMapper
import com.basicfu.sip.base.model.dto.AppDto
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
import com.basicfu.sip.core.util.HttpUtil
import com.basicfu.sip.core.util.RedisUtil
import com.github.pagehelper.PageInfo
import org.apache.ibatis.session.RowBounds
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author basicfu
 * @date 2018/7/9
 */
@Service
class ResourceService : BaseService<ResourceMapper, Resource>() {
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

    /**
     * 同步url
     * type1预览2执行、appId、url
     */
    @Suppress("UNCHECKED_CAST")
    fun sync(vo: ResourceVo): Result<Any> {
        val insertResource = arrayListOf<Resource>()
        val deleteIds = arrayListOf<Long>()
        val existResources = mapper.select(generate {
            appId = vo.appId
        }).associateBy { it.url + it.method }.toMutableMap()
        var insertCount = 0
        val insertDetail = arrayListOf<ResourceDto>()
        val deleteDetail = arrayListOf<ResourceDto>()
        val list = JSON.parseObject(HttpUtil.get(vo.url!!)).getJSONArray("data")
        list.forEach {
            val obj = JSON.parseObject(it.toString())
            val urls = obj.getJSONArray("url")
            val methods = obj.getJSONArray("requestMethod")
            urls.forEach { url ->
                methods.forEach { method ->
                    val resource = existResources["$url$method"]
                    if (resource == null) {
                        val po = generate<Resource> {
                            appId = vo.appId
                            this.url = url.toString()
                            this.method = method.toString()
                            cdate = (java.lang.System.currentTimeMillis() / 1000).toInt()
                            udate = cdate
                            name = ""
                        }
                        insertResource.add(po)
                        insertCount++
                        insertDetail.add(to<ResourceDto>(po)!!)
                    }
                    existResources.remove("$url$method")
                }
            }
        }
        val ids = existResources.values.map { it.id!! }
        val deleteCount = ids.size
        deleteDetail.addAll(to(existResources.values.toList()))
        deleteIds.addAll(ids)
        val item = JSONObject()
        item["insertCount"] = insertCount
        item["deleteCount"] = deleteCount
        item["insertDetail"] = insertDetail
        item["deleteDetail"] = deleteDetail
        return if (vo.type == 2) {
            if (insertResource.isNotEmpty()) {
                mapper.insertList(insertResource)
            }
            if (deleteIds.isNotEmpty()) {
                delete(deleteIds)
            }
            roleService.refreshRolePermission()
            Result.success(null, "操作成功")
        } else {
            Result.success(item)
        }
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
