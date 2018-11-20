package com.basicfu.sip.common.util

import com.basicfu.sip.common.constant.Constant
import com.basicfu.sip.common.model.dto.AppDto
import com.basicfu.sip.common.model.dto.ResourceDto
import com.basicfu.sip.common.model.dto.RoleDto
import com.basicfu.sip.common.model.po.MenuResource
import com.basicfu.sip.common.model.po.PermissionResource
import com.basicfu.sip.common.model.po.RoleMenu
import com.basicfu.sip.common.model.po.RolePermission
import com.basicfu.sip.common.model.redis.RoleToken
import com.basicfu.sip.core.common.mapper.generate
import com.basicfu.sip.core.util.RedisUtil

object PermissionUtil {
    /**
     * 根据数据初始化角色权限
     */
    fun initRolePermission(
        apps: List<AppDto>,
        roles: List<RoleDto>,
        resources: List<ResourceDto>,
        roleMenus: List<RoleMenu>,
        rolePermissions: List<RolePermission>,
        menuResources: List<MenuResource>,
        permissionResources: List<PermissionResource>
    ) {
        val roleMap = roles.groupBy { it.appId!! }
        val roleMenuMap = roleMenus.groupBy { it.appId!! }
        val rolePermissionMap = rolePermissions.groupBy { it.appId!! }
        val menuResourceMap = menuResources.groupBy { it.appId!! }
        val permissionResourceMap = permissionResources.groupBy { it.appId!! }
        apps.forEach { app ->
            val appId = app.id!!
            val roleTokens = arrayListOf<RoleToken>()
            val appRoles = roleMap[appId] ?: arrayListOf()
            val appRoleMenuMap = roleMenuMap[appId]?.groupBy({ it.roleId!! }, { it.menuId!! }) ?: hashMapOf()
            val appRolePermissionMap =
                rolePermissionMap[appId]?.groupBy({ it.roleId!! }, { it.permissionId!! }) ?: hashMapOf()
            val appMenuResources = menuResourceMap[appId] ?: arrayListOf()
            val appPermissionResources = permissionResourceMap[appId] ?: arrayListOf()
            appRoles.forEach { role ->
                val menus = appRoleMenuMap[role.id!!] ?: arrayListOf()
                val permissions = appRolePermissionMap[role.id!!] ?: arrayListOf()
                val resourceIds = arrayListOf<Long>()
                if (menus.isNotEmpty()) {
                    resourceIds.addAll(appMenuResources.filter { menus.contains(it.menuId) }.map { it.resourceId!! })
                }
                if (permissions.isNotEmpty()) {
                    resourceIds.addAll(appPermissionResources.filter { permissions.contains(it.permissionId) }.map { it.resourceId!! })
                }
                val resourcesServiceMap = resources.filter { resourceIds.contains(it.id) }
                    .groupBy({ it.serviceId!! }, { "/" + it.method + it.url })
                roleTokens.add(generate {
                    this.roleCode = role.code
                    this.menus = menus.toSet()
                    this.permissions = permissions.toSet()
                    this.resources = resourcesServiceMap
                })
            }
            //需注意:每个应用初始化后必有一个访客角色，所以这里每个应用都会set进去，一定不会为空，如果手动修改数据库后续获取时未判断是否为空
            RedisUtil.hMSet(Constant.Redis.ROLE_PERMISSION + app.code, roleTokens.associateBy { it.roleCode!! })
        }
    }

}
