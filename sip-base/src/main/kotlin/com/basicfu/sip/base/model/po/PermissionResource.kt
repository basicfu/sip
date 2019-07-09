package com.basicfu.sip.base.model.po

import javax.persistence.*

@Table(name = "permission_resource")
class PermissionResource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    @Column(name = "app_id")
    var appId: Long? = null
    @Column(name = "permission_id")
    var permissionId: Long? = null
    @Column(name = "resource_id")
    var resourceId: Long? = null
    var cdate: Int? = null
}
