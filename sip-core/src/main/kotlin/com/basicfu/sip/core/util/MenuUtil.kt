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
}
