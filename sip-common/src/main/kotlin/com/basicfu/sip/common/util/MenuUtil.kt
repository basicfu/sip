package com.basicfu.sip.common.util

import com.basicfu.sip.common.model.dto.MenuDto

object MenuUtil {
    /**
     * 递归拼成树
     */
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

    /**
     * 根据指定id查找
     */
    fun getItem(menus: List<MenuDto>?, id: Long): MenuDto? {
        if (menus == null) {
            return null
        }
        menus.forEach { menu ->
            if (menu.id == id) {
                return menu
            }
            if (menu.children != null && menu.children!!.isNotEmpty()) {
                val item = getItem(menu.children, id)
                if (item != null) {
                    return item
                }
            }
        }
        return null
    }

    /**
     * 根据指定ids查找
     */
    fun getItems(menus: List<MenuDto>?, ids: List<Long>): List<MenuDto>? {
        val list = arrayListOf<MenuDto>()
        if (menus == null) {
            return list
        }
        menus.forEach { menu ->
            if (ids.contains(menu.id)) {
                list.add(menu)
            }
            if (menu.children != null && menu.children!!.isNotEmpty()) {
                getItems(menu.children, ids)?.let { list.addAll(it) }
            }
        }
        return list
    }

    /**
     * 获取所有children集合
     */
    fun getChildren(menus: List<MenuDto>?): List<MenuDto> {
        val childList = arrayListOf<MenuDto>()
        if (menus == null) {
            return childList
        }
        menus.forEach { menu ->
            childList.add(menu)
            childList.addAll(getChildren(menu.children))
        }
        return childList
    }
}
