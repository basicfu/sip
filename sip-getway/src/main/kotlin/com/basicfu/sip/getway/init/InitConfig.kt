package com.basicfu.sip.getway.init

import com.basicfu.sip.common.constant.Constant
import com.basicfu.sip.common.model.dto.AppDto
import com.basicfu.sip.common.util.PermissionUtil
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
        val roles = mapper.selectRole()
        val resources = mapper.selectResource()
        val roleMenus = mapper.selectRoleMenu()
        val rolePermissions = mapper.selectRolePermission()
        val menuResources = mapper.selectMenuResource()
        val permissionResources = mapper.selectPermissionResource()
        PermissionUtil.initRolePermission(
            apps,
            roles,
            resources,
            roleMenus,
            rolePermissions,
            menuResources,
            permissionResources
        )
    }
}
