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
    var menus: List<Long> = arrayListOf()
    /**
     * 权限ID
     */
    var permissions: List<Long> = arrayListOf()
    /**
     * 资源
     */
    var resources: Map<Long, List<String>> = hashMapOf()
}
