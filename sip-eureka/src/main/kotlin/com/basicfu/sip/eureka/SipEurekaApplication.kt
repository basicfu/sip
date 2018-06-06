package com.basicfu.sip.eureka

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer

@EnableEurekaServer
@SpringBootApplication
class SipEurekaApplication

fun main(args: Array<String>) {
    SpringApplication.run(SipEurekaApplication::class.java, *args)
}