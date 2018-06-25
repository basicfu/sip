package com.basicfu.sip.getway

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.feign.EnableFeignClients
import org.springframework.cloud.netflix.zuul.EnableZuulProxy
import org.springframework.context.annotation.ComponentScan

@ComponentScan(basePackages = ["com.basicfu.sip"])
@EnableZuulProxy
@SpringBootApplication
@EnableFeignClients
class SipGetwayApplication

fun main(args: Array<String>) {
    SpringApplication.run(SipGetwayApplication::class.java, *args)
}