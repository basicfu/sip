package com.basicfu.sip.getway.init

import com.alibaba.fastjson.JSON
import com.basicfu.sip.core.common.Constant
import com.basicfu.sip.core.model.dto.AppDto
import com.basicfu.sip.core.model.dto.ResourceDto
import com.basicfu.sip.core.model.dto.UserDto
import com.basicfu.sip.core.util.MenuUtil
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
        initNoLoginToken(apps)
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
     * 为每个应用初始化未登录用户token永不过期
     * 权限、菜单区分应用，虽然资源同样区分应用，但是其他应用能拥有SIP服务资源
     */
    fun initNoLoginToken(apps: List<AppDto>) {
        DataSourceContextHolder.permission()
        val menuResources = mapper.selectMenuResourceIdByRoleCode(Constant.System.GUEST)
        val permissionResources = mapper.selectPermissionResourceIdByRoleCode(Constant.System.GUEST)
        val menus = mapper.selectMenuByRoleCode(Constant.System.GUEST)
        val resources = arrayListOf<ResourceDto>()
        resources.addAll(menuResources)
        resources.addAll(permissionResources)
        val appResources = resources.distinct().groupBy({ it.appId.toString() }, { it })
        val appMenus = menus.distinct().groupBy({ it.appId.toString() }, { it })
        apps.forEach { app ->
            val appResource = appResources[app.id.toString()]
            val appMenu = MenuUtil.recursive(null, appMenus[app.id.toString()])
            val user = UserDto()
            user.username = Constant.System.GUEST
            user.resources = appResource?.groupBy({ it.serviceId.toString() }, { "/" + it.method + it.url })
            user.menus = JSON.parseArray(JSON.toJSONString(appMenu))
            RedisUtil.set("${Constant.Redis.TOKEN_GUEST}${app.id}", user)
        }
    }
}
