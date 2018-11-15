package com.basicfu.sip.getway.config

import com.basicfu.sip.common.constant.Constant
import com.basicfu.sip.common.model.dto.AppDto
import com.basicfu.sip.core.util.RedisUtil
import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties
import org.springframework.util.StringUtils

class CustomRouteLocator(servletPath: String, private val properties: ZuulProperties) :
    SimpleRouteLocator(servletPath, properties), RefreshableRouteLocator {

    override fun refresh() {
        doRefresh()
    }

    override fun locateRoutes(): Map<String, ZuulProperties.ZuulRoute> {
        val routesMap = LinkedHashMap<String, ZuulProperties.ZuulRoute>()
        routesMap.putAll(super.locateRoutes())
        val appServices = RedisUtil.hGetAll<AppDto>(Constant.Redis.APP)
        //初始化路由时加入app domain，所有url由domain+prefix+url组成
        appServices.forEach { _, app ->
            app?.services?.forEach { v ->
                val zuulRoute = ZuulProperties.ZuulRoute()
                zuulRoute.id = v.id.toString()
                zuulRoute.path = "/" + app.code + v.path
                zuulRoute.serviceId = v.serverId
                zuulRoute.url = v.url
                zuulRoute.isStripPrefix = v.stripPrefix!!
                zuulRoute.retryable = v.retryable
                routesMap[zuulRoute.path] = zuulRoute
            }
        }
        val values = LinkedHashMap<String, ZuulProperties.ZuulRoute>()
        for (entry in routesMap.entries) {
            var path = entry.key
            // Prepend with slash if not already present.
            if (!path.startsWith("/")) {
                path = "/$path"
            }
            if (StringUtils.hasText(this.properties.prefix)) {
                path = this.properties.prefix + path
                if (!path.startsWith("/")) {
                    path = "/$path"
                }
            }
            values[path] = entry.value
        }
        return values
    }
}
