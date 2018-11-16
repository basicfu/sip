package com.basicfu.sip.common.model.redis

class RoleToken {
    /**
     * 角色Code
     */
    var roleCode: String? = null
    /**
     * 菜单ID
     */
    var menus: List<Long>? = null
    /**
     * 权限ID
     */
    var permissions: List<Long>? = null
    /**
     * 资源
     */
    var resources: Map<Long, List<String>>? = null
}
