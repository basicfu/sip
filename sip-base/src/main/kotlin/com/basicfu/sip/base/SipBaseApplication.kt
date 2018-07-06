package com.basicfu.sip.base

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.feign.EnableFeignClients
import org.springframework.context.annotation.ComponentScan
import tk.mybatis.spring.annotation.MapperScan

@MapperScan(basePackages = ["com.basicfu.sip.base.mapper"])
@ComponentScan(basePackages = ["com.basicfu.sip"])
@EnableFeignClients(basePackages = ["com.basicfu.sip"])
@SpringBootApplication

class SipBaseApplication

fun main(args: Array<String>) {
    SpringApplication.run(SipBaseApplication::class.java, *args)
}
