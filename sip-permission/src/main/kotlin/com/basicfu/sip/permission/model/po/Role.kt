package com.basicfu.sip.permission.model.po

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class Role {
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
     * 角色名
     */
    /**
     * 获取角色名
     *
     * @return name - 角色名
     */
    /**
     * 设置角色名
     *
     * @param name 角色名
     */
    var name: String? = null
    var code: String? = null

    /**
     * 是否启用
     */
    /**
     * 获取是否启用
     *
     * @return enalbe - 是否启用
     */
    /**
     * 设置是否启用
     *
     * @param enalbe 是否启用
     */
    var enalbe: Int? = null
}
