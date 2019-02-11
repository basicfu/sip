package com.basicfu.sip.base.service

import com.basicfu.sip.base.mapper.MenuMapper
import com.basicfu.sip.base.mapper.MenuResourceMapper
import com.basicfu.sip.base.mapper.ResourceMapper
import com.basicfu.sip.base.model.po.Menu
import com.basicfu.sip.base.model.po.Resource
import com.basicfu.sip.base.model.vo.MenuVo
import com.basicfu.sip.common.constant.Constant
import com.basicfu.sip.common.enum.Enum
import com.basicfu.sip.common.model.dto.MenuDto
import com.basicfu.sip.common.model.dto.ResourceDto
import com.basicfu.sip.common.model.po.MenuResource
import com.basicfu.sip.common.util.AppUtil
import com.basicfu.sip.common.util.MenuUtil
import com.basicfu.sip.core.common.exception.CustomException
import com.basicfu.sip.core.common.mapper.example
import com.basicfu.sip.core.common.mapper.generate
import com.basicfu.sip.core.service.BaseService
import com.basicfu.sip.core.util.SqlUtil
import com.github.pagehelper.PageInfo
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author basicfu
 * @date 2018/7/9
 */
@Service
class MenuService : BaseService<MenuMapper, Menu>() {
    @Autowired
    lateinit var mrMapper: MenuResourceMapper
    @Autowired
    lateinit var resourceMapper: ResourceMapper
    @Autowired
    lateinit var menuResourceMapper: MenuResourceMapper
    @Autowired
    lateinit var roleService: RoleService

    fun all(): List<MenuDto> {
        val menus = to<MenuDto>(mapper.selectAll())
        val ids = menus.map { it.id }
        if (ids.isNotEmpty()) {
            val menuResourceMap = menuResourceMapper.selectByExample(example<MenuResource> {
                select(MenuResource::menuId, MenuResource::resourceId)
                andIn(MenuResource::menuId, ids)
            }).groupBy { it.menuId }
            menus.forEach {
                it.resourceCount = menuResourceMap[it.id!!]?.size?.toLong() ?: 0
            }
        }
        return MenuUtil.recursive(null, menus)
    }

    /**
     * 不检查应用，临时解决方案
     */
    fun listByIds(ids: List<Long>): List<MenuDto> {
        AppUtil.notCheckApp()
        return to(selectByIds(ids))
    }

    fun listResourceById(id: Long, q: String?): PageInfo<ResourceDto> {
        val likeValue = SqlUtil.dealLikeValue(q)
        startPage()
        val appIds = arrayListOf(AppUtil.getAppId())
        val appCode = AppUtil.getAppCode()
        if (Constant.System.APP_SYSTEM_CODE != appCode) {
            appIds.add(AppUtil.getAppIdByAppCode(Constant.System.APP_SYSTEM_CODE))
        }
        var sql =
            "select r.id as id,service_id as serviceId,url,method,name,mr.cdate as cdate from menu_resource mr LEFT JOIN resource r on mr.resource_id=r.id WHERE mr.menu_id=$id" +
                    " and mr.app_id in (${StringUtils.join(appIds, ",")}) and r.app_id in (${StringUtils.join(
                        appIds,
                        ","
                    )})"
        likeValue?.let { sql += " and (r.url like $likeValue or r.name like $likeValue)" }
        sql += " ORDER BY mr.cdate DESC"
        AppUtil.notCheckApp(2)
        val result = resourceMapper.selectBySql(sql)
        return getPageInfo(result)
    }

    /**
     * 内部使用,根据菜单ids获取resourceIds
     */
//    fun listResourceIdsByMenuIds(ids: List<Long>): List<Long> {
//        return menuResourceMapper.selectByExample(example<MenuResource> {
//            andIn(MenuResource::menuId,ids)
//        }).map { it.resourceId!! }
//    }

    fun insert(vo: MenuVo): Int {
        if (mapper.selectCount(generate {
                name = vo.name
            }) != 0) throw CustomException(Enum.EXIST_MENU_NAME)
        val po = dealInsert(to<Menu>(vo))
        mapper.updateBySql("set sort=sort+1 where pid=${vo.pid} and sort>=${vo.sort}")
        return mapper.insertSelective(po)
    }

    fun insertResource(vo: MenuVo): Int {
        var ids = vo.resourceIds!!
        AppUtil.notCheckApp()
        val appIds = arrayListOf(AppUtil.getAppId())
        val appCode = AppUtil.getAppCode()
        if (Constant.System.APP_SYSTEM_CODE != appCode) {
            appIds.add(AppUtil.getAppIdByAppCode(Constant.System.APP_SYSTEM_CODE))
        }
        if (resourceMapper.selectCountByExample(example<Resource> {
                andIn(Resource::appId, appIds)
                andIn(Resource::id, ids)
            }) != ids.size) throw CustomException(Enum.NOT_FOUND_RESOURCE)
        val existsResourceIds = menuResourceMapper.selectByExample(example<MenuResource> {
            andEqualTo(MenuResource::menuId, vo.id)
            andIn(MenuResource::resourceId, ids)
        }).map { it.resourceId }
        ids = ids.filter { !existsResourceIds.contains(it) }
        if (ids.isEmpty()) {
            throw CustomException(Enum.EXIST_ADD_DATA)
        }
        val menuResources = arrayListOf<MenuResource>()
        ids.forEach { it ->
            val mr = MenuResource()
            mr.menuId = vo.id
            mr.resourceId = it
            menuResources.add(dealInsert(mr))
        }
        val count = mrMapper.insertList(menuResources)
        roleService.refreshRolePermission()
        return count
    }

    fun update(vo: MenuVo): Int {
        val checkMenu = mapper.selectOne(generate {
            name = vo.name
        })
        if (checkMenu != null && checkMenu.id != vo.id) throw CustomException(Enum.EXIST_MENU_NAME)
        val po = dealUpdate(to<Menu>(vo))
        val count = mapper.updateByPrimaryKeySelective(po)
        roleService.refreshRolePermission()
        return count
    }

    fun updateDisplay(id: Long, display: Boolean): Int {
        val po = dealUpdate(generate<Menu> {
            this.id = id
            this.display = display
        })
        return mapper.updateByPrimaryKeySelective(po)
    }

    fun updateSort(dragId: Long, hoverId: Long): Int {
        val menuMap = selectByIds(arrayListOf(dragId, hoverId)).associateBy { it.id!! }
        if (menuMap.size != 2) {
            throw CustomException(Enum.NOT_FOUND_MENU_ID)
        }
        val dragMenu = menuMap[dragId]!!
        val hoverMenu = menuMap[hoverId]!!
        if (dragMenu.pid!! != hoverMenu.pid!!) {
            throw CustomException(Enum.DRAG_SORT_NEED_SAME_LEVEL)
        }
        mapper.updateBySql("set sort=sort+1 where pid=${dragMenu.pid} and sort>=${hoverMenu.sort}")
        mapper.updateBySql("set sort=sort-1 where pid=${dragMenu.pid} and sort<${hoverMenu.sort}")
        dragMenu.sort = hoverMenu.sort
        mapper.updateByPrimaryKeySelective(dragMenu)
        return 1
    }

    fun delete(ids: List<Long>): Int {
        var deleteCount = 0
        if (ids.isNotEmpty()) {
            val deleteIds = arrayListOf<Long>()
            val menus = to<MenuDto>(mapper.selectAll())
            val recursive = MenuUtil.recursive(null, menus)
            var menu: MenuDto?
            ids.forEach { id ->
                menu = MenuUtil.getItem(recursive, id)
                if (menu != null) {
                    val toMutableList = MenuUtil.getChildren(menu!!.children).map { it.id!! }.toMutableList()
                    toMutableList.add(menu!!.id!!)
                    deleteIds.addAll(toMutableList)
                }
            }
            deleteByIds(deleteIds)
            deleteCount = deleteIds.size
        }
        roleService.refreshRolePermission()
        return deleteCount
    }

    fun deleteResource(vo: MenuVo): Int {
        val count = mrMapper.deleteByExample(example<MenuResource> {
            andEqualTo(MenuResource::menuId, vo.id)
            andIn(MenuResource::resourceId, vo.resourceIds!!)
        })
        roleService.refreshRolePermission()
        return count
    }
}
