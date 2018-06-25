package com.basicfu.sip.getway.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author basicfu
 * @date 2018/6/22
 */
@Configuration
class DynamicRoute {
    @Autowired
    internal var zuulProperties: ZuulProperties? = null
    @Autowired
    internal var server: ServerProperties? = null

    @Bean
    fun routeLocator(): CustomRouteLocator? {
        return zuulProperties?.let { CustomRouteLocator(server!!.servletPrefix, it) }
    }

}
