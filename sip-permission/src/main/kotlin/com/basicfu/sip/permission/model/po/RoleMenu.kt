package com.basicfu.sip.permission.model.po

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "role_menu")
class RoleMenu {
    /**
     * @return id
     */
    /**
     * @param id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    /**
     * 角色ID
     */
    /**
     * 获取角色ID
     *
     * @return role_id - 角色ID
     */
    /**
     * 设置角色ID
     *
     * @param roleId 角色ID
     */
    @Column(name = "role_id")
    var roleId: Long? = null

    /**
     * 菜单ID
     */
    /**
     * 获取菜单ID
     *
     * @return menu_id - 菜单ID
     */
    /**
     * 设置菜单ID
     *
     * @param menuId 菜单ID
     */
    @Column(name = "menu_id")
    var menuId: Long? = null
}
