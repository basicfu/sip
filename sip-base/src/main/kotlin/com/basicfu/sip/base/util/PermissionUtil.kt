package com.basicfu.sip.base.util

import com.basicfu.sip.base.common.constant.Constant
import com.basicfu.sip.base.model.biz.RoleToken
import com.basicfu.sip.base.model.dto.AppDto
import com.basicfu.sip.base.model.dto.ResourceDto
import com.basicfu.sip.base.model.dto.RoleDto
import com.basicfu.sip.base.model.po.MenuResource
import com.basicfu.sip.base.model.po.PermissionResource
import com.basicfu.sip.base.model.po.RoleMenu
import com.basicfu.sip.base.model.po.RolePermission
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
        val appsMap = apps.associateBy({ it.id }, { it.code })
        val roleTokens = arrayListOf<RoleToken>()
        val roleMenuMap = roleMenus.groupBy({ it.roleId!! }, { it.menuId!! })
        val rolePermissionMap = rolePermissions.groupBy({ it.roleId!! }, { it.permissionId!! })
        roles.forEach { role ->
            val menus = roleMenuMap[role.id!!] ?: arrayListOf()
            val permissions = rolePermissionMap[role.id!!] ?: arrayListOf()
            val resourceIds = arrayListOf<Long>()
            if (menus.isNotEmpty()) {
                resourceIds.addAll(menuResources.filter { menus.contains(it.menuId) }.map { it.resourceId!! })
            }
            if (permissions.isNotEmpty()) {
                resourceIds.addAll(permissionResources.filter { permissions.contains(it.permissionId) }.map { it.resourceId!! })
            }
            val resourcesMap = hashMapOf<String, List<String>>()
            resources.filter { resourceIds.contains(it.id) }.groupBy({ it.appId!! }, { "/" + it.method + it.url })
                .forEach { k, v ->
                    appsMap[k]?.let { resourcesMap[it] = v }
                }
            roleTokens.add(generate {
                this.roleCode = role.code
                this.menus = menus.toSet()
                this.permissions = permissions.toSet()
                this.resources = resourcesMap
            })
        }
        //需注意:初始化后必有一个访客角色一定不会为空，如果手动修改数据库后续获取时未判断是否为空
        RedisUtil.hMSet(Constant.Redis.ROLE_PERMISSION, roleTokens.associateBy { it.roleCode!! })
    }

}
