package com.basicfu.sip.log

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.zuul.EnableZuulProxy

@EnableZuulProxy
@SpringBootApplication
class SipGetwayApplication

fun main(args: Array<String>) {
    SpringApplication.run(SipGetwayApplication::class.java, *args)
}