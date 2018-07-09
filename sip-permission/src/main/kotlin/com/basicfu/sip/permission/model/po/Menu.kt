package com.basicfu.sip.permission.model.po

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class Menu {
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
     * 应用Id
     */
    /**
     * 获取应用Id
     *
     * @return app_id - 应用Id
     */
    /**
     * 设置应用Id
     *
     * @param appId 应用Id
     */
    @Column(name = "app_id")
    var appId: Long? = null

    /**
     * 父级Id
     */
    /**
     * 获取父级Id
     *
     * @return pid - 父级Id
     */
    /**
     * 设置父级Id
     *
     * @param pid 父级Id
     */
    var pid: Long? = null

    /**
     * 菜单名
     */
    /**
     * 获取菜单名
     *
     * @return name - 菜单名
     */
    /**
     * 设置菜单名
     *
     * @param name 菜单名
     */
    var name: String? = null

    /**
     * 菜单路径
     */
    /**
     * 获取菜单路径
     *
     * @return path - 菜单路径
     */
    /**
     * 设置菜单路径
     *
     * @param path 菜单路径
     */
    var path: String? = null

    /**
     * 菜单顺序
     */
    /**
     * 获取菜单顺序
     *
     * @return sort - 菜单顺序
     */
    /**
     * 设置菜单顺序
     *
     * @param sort 菜单顺序
     */
    var sort: Int? = null

    /**
     * 菜单图标
     */
    /**
     * 获取菜单图标
     *
     * @return icon - 菜单图标
     */
    /**
     * 设置菜单图标
     *
     * @param icon 菜单图标
     */
    var icon: String? = null

    /**
     * 菜单类型(页面,元素)
     */
    /**
     * 获取菜单类型(页面,元素)
     *
     * @return type - 菜单类型(页面,元素)
     */
    /**
     * 设置菜单类型(页面,元素)
     *
     * @param type 菜单类型(页面,元素)
     */
    var type: String? = null

    /**
     * 是否显示
     */
    /**
     * 获取是否显示
     *
     * @return display - 是否显示
     */
    /**
     * 设置是否显示
     *
     * @param display 是否显示
     */
    var display: Int? = null
}
