package com.basicfu.sip.core.util

import com.basicfu.sip.core.model.dto.MenuDto

object MenuUtil {
    fun recursive(parentMenu: MenuDto?, menus: List<MenuDto>?): List<MenuDto> {
        val childList = arrayListOf<MenuDto>()
        if (menus == null) {
            return childList
        }
        if (parentMenu == null) {
            val rootMenus = menus.filter { it.pid == 0L }
            childList.addAll(rootMenus)
            childList.sortBy { it.sort }
            rootMenus.forEach { menu ->
                recursive(menu, menus)
            }
        } else {
            menus.forEach { menu ->
                if (menu.pid == parentMenu.id) {
                    childList.add(menu)
                    recursive(menu, menus)
                }
            }
            childList.sortBy { it.sort }
            parentMenu.children = childList
        }
        return childList
    }
    fun getItem(menus: List<MenuDto>?,id:Long):MenuDto?{
        if (menus == null) {
            return null
        }
        menus.forEach { menu->
            if(menu.id==id){
                return menu
            }
            if(menu.children!=null&&menu.children!!.isNotEmpty()){
                val item = getItem(menu.children, id)
                if(item!=null){
                    return item
                }
            }
        }
        return null
    }
    fun getChildren(menus: List<MenuDto>?):List<MenuDto>{
        val childList = arrayListOf<MenuDto>()
        if (menus == null) {
            return childList
        }
        menus.forEach { menu->
            childList.add(menu)
            childList.addAll(getChildren(menu.children))
        }
        return childList
    }
}
