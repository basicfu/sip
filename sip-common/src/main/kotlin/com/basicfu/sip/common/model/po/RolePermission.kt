package com.basicfu.sip.common.model.po

import javax.persistence.*

@Table(name = "role_permission")
class RolePermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    @Column(name = "app_id")
    var appId: Long? = null
    @Column(name = "role_id")
    var roleId: Long? = null
    @Column(name = "permission_id")
    var permissionId: Long? = null
    var cdate: Int? = null
}
