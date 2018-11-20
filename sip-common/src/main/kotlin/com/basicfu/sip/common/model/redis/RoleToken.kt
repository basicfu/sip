package com.basicfu.sip.common.model.redis

/**
 * 由于序列化时会将空list序列化为空，bean默认使用空list
 */
class RoleToken {
    /**
     * 角色Code
     */
    var roleCode: String? = null
    /**
     * 菜单ID
     */
    var menus: Set<Long> = linkedSetOf()
    /**
     * 权限ID
     */
    var permissions: Set<Long> = linkedSetOf()
    /**
     * 资源
     */
    var resources: Map<Long, List<String>> = linkedMapOf()
}
