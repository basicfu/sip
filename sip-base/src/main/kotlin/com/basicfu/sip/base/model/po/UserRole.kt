package com.basicfu.sip.base.model.po

import javax.persistence.*

@Table(name = "user_role")
class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    @Column(name = "user_id")
    var userId: String? = null
    @Column(name = "role_id")
    var roleId: Long? = null
    var cdate: Int? = null
}
