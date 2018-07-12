package com.basicfu.sip.getway.config

import com.basicfu.sip.core.Constant
import com.basicfu.sip.core.model.dto.ApplicationDto
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
        val all = RedisUtil.hGetAll<ApplicationDto>(Constant.Redis.SERVICE)
        all.forEach { _, v ->
            if (v != null) {
                val zuulRoute = ZuulProperties.ZuulRoute()
                zuulRoute.id = v.id.toString()
                zuulRoute.path = v.path
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
