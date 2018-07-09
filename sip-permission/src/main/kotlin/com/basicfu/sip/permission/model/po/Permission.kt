package com.basicfu.sip.permission.model.po

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class Permission {
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
     * 权限名
     */
    /**
     * 获取权限名
     *
     * @return name - 权限名
     */
    /**
     * 设置权限名
     *
     * @param name 权限名
     */
    var name: String? = null
}
