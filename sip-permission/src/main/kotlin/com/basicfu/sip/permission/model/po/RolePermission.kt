package com.basicfu.sip.permission.model.po

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "role_permission")
class RolePermission {
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
     * 角色Id
     */
    /**
     * 获取角色Id
     *
     * @return role_id - 角色Id
     */
    /**
     * 设置角色Id
     *
     * @param roleId 角色Id
     */
    @Column(name = "role_id")
    var roleId: Long? = null

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
}
