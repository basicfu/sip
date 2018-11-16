package com.basicfu.sip.getway.init

import com.basicfu.sip.common.constant.Constant
import com.basicfu.sip.common.model.dto.AppDto
import com.basicfu.sip.common.model.redis.RoleToken
import com.basicfu.sip.core.common.mapper.generate
import com.basicfu.sip.core.util.RedisUtil
import com.basicfu.sip.getway.common.datasource.DataSourceContextHolder
import com.basicfu.sip.getway.mapper.Mapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

/**
 * @author basicfu
 * @date 2018/7/12
 */
@Component("getwayInitConfig")
class InitConfig : CommandLineRunner {
    @Autowired
    lateinit var mapper: Mapper

    override fun run(vararg strings: String) {
        val apps = initAppService()
        initRolePermission(apps)
    }

    /**
     * 初始化app和app下的service
     * 此处已经包装路由由domain+prefix组成
     */
    fun initAppService(): List<AppDto> {
        DataSourceContextHolder.base()
        val apps = mapper.selectApp()
        val services = mapper.selectService()
        val secrets = mapper.selectSecret()
        val serviceMap = services.groupBy({ it.appId!! }, { it })
        val secretMap = secrets.groupBy({ it.appId!! }, { it })
        apps.forEach { app ->
            app.services = serviceMap[app.id] ?: arrayListOf()
            app.secrets = secretMap[app.id] ?: arrayListOf()
        }
        RedisUtil.hMSet(Constant.Redis.APP, apps.associateBy({ it.code!! }, { it }))
        return apps
    }

    /**
     * 为每个角色初始化菜单、权限、资源，变化时应实时刷新
     * 权限、菜单区分应用，虽然资源同样区分应用，但是其他应用能拥有SIP服务资源
     */
    fun initRolePermission(apps: List<AppDto>) {
        DataSourceContextHolder.permission()
        val roleMap = mapper.selectRole().groupBy { it.appId!! }
        val resources = mapper.selectResource()
        val roleMenuMap = mapper.selectRoleMenu().groupBy { it.appId!! }
        val rolePermissionMap = mapper.selectRolePermission().groupBy { it.appId!! }
        val menuResources = mapper.selectMenuResource().groupBy { it.appId!! }
        val permissionResources = mapper.selectPermissionResource().groupBy { it.appId!! }
        apps.forEach { app ->
            val appId = app.id!!
            val roleTokens = arrayListOf<RoleToken>()
            val appRoles = roleMap[appId] ?: arrayListOf()
            val appRoleMenuMap = roleMenuMap[appId]?.groupBy({ it.roleId!! }, { it.menuId!! }) ?: hashMapOf()
            val appRolePermissionMap =
                rolePermissionMap[appId]?.groupBy({ it.roleId!! }, { it.permissionId!! }) ?: hashMapOf()
            val appMenuResources = menuResources[appId] ?: arrayListOf()
            val appPermissionResources = permissionResources[appId] ?: arrayListOf()
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
                    this.menus = menus
                    this.permissions = permissions
                    this.resources = resourcesServiceMap
                })
            }
            RedisUtil.hMSet(Constant.Redis.ROLE_PERMISSION + app.code, roleTokens.associateBy { it.roleCode!! })
        }
    }
}
