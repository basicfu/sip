package com.basicfu.sip.permission.model.po

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "menu_resource")
class MenuResource {
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
     * 菜单Id
     */
    /**
     * 获取菜单Id
     *
     * @return menu_id - 菜单Id
     */
    /**
     * 设置菜单Id
     *
     * @param menuId 菜单Id
     */
    @Column(name = "menu_id")
    var menuId: Long? = null

    /**
     * 资源Id
     */
    /**
     * 获取资源Id
     *
     * @return resource_id - 资源Id
     */
    /**
     * 设置资源Id
     *
     * @param resourceId 资源Id
     */
    @Column(name = "resource_id")
    var resourceId: Long? = null
}
