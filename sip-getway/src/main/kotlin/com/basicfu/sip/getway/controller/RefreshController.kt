package com.basicfu.sip.getway.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.netflix.zuul.RoutesRefreshedEvent
import org.springframework.cloud.netflix.zuul.filters.RouteLocator
import org.springframework.context.ApplicationEventPublisher
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RefreshController {
    @Autowired
    lateinit var publisher: ApplicationEventPublisher
    @Autowired
    lateinit var routeLocator: RouteLocator

    @GetMapping("/refreshRoute")
    fun refresh(): String {
        publisher.publishEvent(RoutesRefreshedEvent(routeLocator))
        return "refresh success"
    }
} 