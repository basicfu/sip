package com.basicfu.sip.permission.model.po

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "user_role")
class UserRole {
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
     * 用户Id
     */
    /**
     * 获取用户Id
     *
     * @return user_id - 用户Id
     */
    /**
     * 设置用户Id
     *
     * @param userId 用户Id
     */
    @Column(name = "user_id")
    var userId: Long? = null

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
}
