package com.basicfu.sip.getway

import com.basicfu.sip.core.annotation.EnableSipCore
import com.basicfu.sip.core.common.Function
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.cloud.client.SpringCloudApplication
import org.springframework.cloud.netflix.hystrix.EnableHystrix
import org.springframework.cloud.netflix.zuul.EnableZuulProxy

@EnableZuulProxy
@ServletComponentScan
@EnableHystrix
@EnableSipCore(disable = [Function.DataSource])
@SpringCloudApplication
class SipGetwayApplication

fun main(args: Array<String>) {
    runApplication<SipGetwayApplication>(*args)
}
