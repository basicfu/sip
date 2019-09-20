package com.basicfu.sip.base.model.po

import javax.persistence.*

@Table(name = "user_role")
class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    @Column(name = "username")
    var username: String? = null
    @Column(name = "role_id")
    var roleId: Long? = null
    var cdate: Int? = null
}
