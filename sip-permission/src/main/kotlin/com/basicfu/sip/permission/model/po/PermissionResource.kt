package com.basicfu.sip.permission.model.po

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "permission_resource")
class PermissionResource {
    /**
     * @return id
     */
    /**
     * @param id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name = "app_id")
    var appId: Long? = null
    /**
     * 权限Id
     */
    /**
     * 获取权限Id
     *
     * @return permission_id - 权限Id
     */
    /**
     * 设置权限Id
     *
     * @param permissionId 权限Id
     */
    @Column(name = "permission_id")
    var permissionId: Long? = null

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
