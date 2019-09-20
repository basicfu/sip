package com.basicfu.sip.base

import com.basicfu.sip.client.annotation.EnableSipClient
import com.basicfu.sip.core.annotation.EnableSipCore
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import tk.mybatis.spring.annotation.MapperScan

@MapperScan(basePackages = ["com.basicfu.sip.base.mapper"])
@EnableFeignClients
@EnableSipCore
@EnableSipClient
//@ServletComponentScan(basePackages = ["com.basicfu.sip"])
@SpringBootApplication
class SipBaseApplication

fun main(args: Array<String>) {
    runApplication<SipBaseApplication>(*args)
}
