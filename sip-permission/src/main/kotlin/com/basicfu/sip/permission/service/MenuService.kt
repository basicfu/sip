package com.basicfu.sip.permission.service

import com.basicfu.sip.core.common.Enum
import com.basicfu.sip.core.common.exception.CustomException
import com.basicfu.sip.core.common.mapper.example
import com.basicfu.sip.core.common.mapper.generate
import com.basicfu.sip.core.model.dto.MenuDto
import com.basicfu.sip.core.service.BaseService
import com.basicfu.sip.core.util.MenuUtil
import com.basicfu.sip.permission.mapper.MenuMapper
import com.basicfu.sip.permission.mapper.MenuResourceMapper
import com.basicfu.sip.permission.mapper.ResourceMapper
import com.basicfu.sip.permission.model.po.Menu
import com.basicfu.sip.permission.model.po.MenuResource
import com.basicfu.sip.permission.model.po.Resource
import com.basicfu.sip.permission.model.vo.MenuVo
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

    fun all(): List<Any> {
        val menus = to<MenuDto>(mapper.selectAll())
        return MenuUtil.recursive(null, menus)
    }

    fun insert(vo: MenuVo): Int {
        if (mapper.selectCount(generate {
                name = vo.name
            }) != 0) throw CustomException(Enum.EXIST_MENU_NAME)
        val po = dealInsert(to<Menu>(vo))
        mapper.updateBySql("set sort=sort+1 where pid=${vo.pid} and sort>=${vo.sort}")
        return mapper.insertSelective(po)
    }

    fun insertResource(vo: MenuVo): Int {
        val ids = vo.resourceIds!!
        if (resourceMapper.selectCountByExample(example<Resource> {
                andIn(Resource::id, ids)
            }) != ids.size) throw CustomException(Enum.NOT_FOUND_RESOURCE)
        val menuResources = arrayListOf<MenuResource>()
        ids.forEach { it ->
            val mr = MenuResource()
            mr.menuId = vo.id
            mr.resourceId = it
            menuResources.add(mr)
        }
        return mrMapper.insertList(menuResources)
    }

    fun update(vo: MenuVo): Int {
        val checkMenu = mapper.selectOne(generate {
            name = vo.name
        })
        if (checkMenu != null && checkMenu.id != vo.id) throw CustomException(Enum.EXIST_MENU_NAME)
        val po = dealUpdate(to<Menu>(vo))
        return mapper.updateByPrimaryKeySelective(po)
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
        if(menuMap.size!=2){
            throw CustomException(Enum.NOT_FOUND_MENU_ID)
        }
        val dragMenu = menuMap[dragId]!!
        val hoverMenu = menuMap[hoverId]!!
        if(dragMenu.pid!!!=hoverMenu.pid!!){
            throw CustomException(Enum.DRAG_SORT_NEED_SAME_LEVEL)
        }
        mapper.updateBySql("set sort=sort+1 where pid=${dragMenu.pid} and sort>=${hoverMenu.sort}")
        mapper.updateBySql("set sort=sort-1 where pid=${dragMenu.pid} and sort<${hoverMenu.sort}")
        dragMenu.sort=hoverMenu.sort
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
        return deleteCount
    }

    fun deleteResource(vo: MenuVo): Int {
        return mrMapper.deleteByExample(example<MenuResource> {
            andEqualTo(MenuResource::menuId, vo.id)
            andIn(MenuResource::resourceId, vo.resourceIds!!)
        })
    }
}
