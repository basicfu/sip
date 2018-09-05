package com.basicfu.sip.getway

import com.basicfu.sip.core.annotation.EnableSipCore
import com.basicfu.sip.core.common.Function
import org.springframework.boot.SpringApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.cloud.client.SpringCloudApplication
import org.springframework.cloud.netflix.feign.EnableFeignClients
import org.springframework.cloud.netflix.zuul.EnableZuulProxy

@EnableZuulProxy
@ServletComponentScan
@EnableFeignClients
@EnableSipCore(disable = [Function.DataSource])
@SpringCloudApplication
class SipGetwayApplication

fun main(args: Array<String>) {
    SpringApplication.run(SipGetwayApplication::class.java, *args)
}
