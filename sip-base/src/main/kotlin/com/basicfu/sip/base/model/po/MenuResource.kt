package com.basicfu.sip.base.model.po

import javax.persistence.*

@Table(name = "menu_resource")
class MenuResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    @Column(name = "menu_id")
    var menuId: Long? = null
    @Column(name = "resource_id")
    var resourceId: Long? = null
    var cdate: Int? = null
}
