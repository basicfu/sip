package com.basicfu.sip.common.model.redis

import com.alibaba.fastjson.JSONArray

class RolePermission {
    /**
     * 角色ID
     */
    var roleId: Long? = null
    /**
     * 菜单ID
     */
    var menus: JSONArray? = null
    /**
     * 权限ID
     */
    var permissions: JSONArray? = null
    /**
     * 资源
     */
    var resources: Map<String, List<String>>? = null
}
