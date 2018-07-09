package com.basicfu.sip.permission.service

import com.basicfu.sip.core.exception.CustomException
import com.basicfu.sip.core.mapper.example
import com.basicfu.sip.core.mapper.generate
import com.basicfu.sip.core.service.BaseService
import com.basicfu.sip.permission.common.Enum
import com.basicfu.sip.permission.mapper.MenuMapper
import com.basicfu.sip.permission.mapper.MenuResourceMapper
import com.basicfu.sip.permission.model.dto.MenuDto
import com.basicfu.sip.permission.model.po.Menu
import com.basicfu.sip.permission.model.po.MenuResource
import com.basicfu.sip.permission.model.po.RoleMenu
import com.basicfu.sip.permission.model.vo.MenuVo
import org.springframework.beans.BeanUtils
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

//    fun list(token:String?): List<Any> {
//        val token = RedisUtil.get(Constant.Redis.TOKEN + token,Token::class.java)
//        if(token!=null) {
//            val menus=selectByIds(token.menus)
//            val result = arrayListOf<MenuDto>()
//            menus.sortBy { it.pid }
//            menus.sortBy { it.sort }
//            menus.forEach { e ->
//                if (0L == e.pid) {
//                    val dto = MenuDto()
//                    BeanUtils.copyProperties(e, dto)
//                    if(dto.url==null)dto.url=""
//                    result.add(dto)
//                }
//            }
//            result.forEach { e ->
//                e.children = getChildren(e, menus)
//            }
//            return result
//        }else{
//            return emptyList()
//        }
//    }

    fun all(): List<Any> {
        val result = arrayListOf<MenuDto>()
        val menus = to<MenuDto>(mapper.selectAll())
        menus.forEach { e ->
            if (0L == e.pid) {
                val dto = to<MenuDto>(e)
                result.add(dto!!)
            }
        }
        result.forEach { e ->
            e.children = chidren(e, menus)
        }
        return result
    }

    fun insert(vo: MenuVo): Int {
        if (mapper.selectCount(generate {
                name = vo.name
            }) != 0) throw CustomException(Enum.Resource.EXIST_URL)
        val po = dealInsert(to<Menu>(vo))
        return mapper.insertSelective(po)
    }

    fun insertResource(vo: MenuVo): Int {
        val ids = vo.resourceIds!!
        if (mrMapper.selectCountByExample(example<RoleMenu> {
                andIn(RoleMenu::menuId, ids)
            }) != ids.size) throw CustomException(Enum.Menu.RESOURCE_NOT_FOUND)
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
        if (checkMenu != null && checkMenu.id != vo.id) throw CustomException(Enum.Resource.EXIST_URL)
        val po = dealUpdate(to<Menu>(vo))
        return mapper.updateByPrimaryKeySelective(po)
    }

    fun delete(ids: List<Long>): Int {
        return deleteByIds(ids)
    }

    fun deleteResource(vo: MenuVo): Int {
        return mrMapper.deleteByExample(example<MenuResource> {
            andEqualTo(MenuResource::menuId, vo.id)
            andIn(MenuResource::resourceId, vo.resourceIds!!)
        })
    }

    private fun chidren(m: MenuDto, menus: List<MenuDto>): List<MenuDto> {
        val childList = arrayListOf<MenuDto>()
        var flag = true
        menus.forEach { e ->
            if (e.pid == m.id) {
                flag = false
                val split = e.path?.split('/')
                m.path = split?.get(split.size - 2)
                val menu = MenuDto()
                BeanUtils.copyProperties(e, menu)
//                menu.value=e.id.toString()
                menu.path = split?.get(split.size - 1)
                menu.children = chidren(menu, menus)
                childList.add(menu)
            }
        }
        if (flag) {
            val split = m.path?.split('/')
            m.path = split?.get(split.size - 1)
        } else {
            if (m.path == null) m.path = ""
        }
        return childList
    }
}
