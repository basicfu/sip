package com.basicfu.sip.dict

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.feign.EnableFeignClients
import org.springframework.context.annotation.ComponentScan
import tk.mybatis.spring.annotation.MapperScan

@MapperScan(basePackages = ["com.basicfu.sip.dict.mapper"])
@ComponentScan(basePackages = ["com.basicfu.sip"])
@SpringBootApplication
@EnableFeignClients
class SipDictApplication

fun main(args: Array<String>) {
    SpringApplication.run(SipDictApplication::class.java, *args)
}