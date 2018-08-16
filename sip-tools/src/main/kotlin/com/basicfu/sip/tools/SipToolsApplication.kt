package com.basicfu.sip.tools

import com.basicfu.sip.core.annotation.EnableSipCore
import org.springframework.boot.SpringApplication
import org.springframework.cloud.client.SpringCloudApplication
import tk.mybatis.spring.annotation.MapperScan

@MapperScan(basePackages = ["com.basicfu.sip.tools.mapper"])
@EnableSipCore
@SpringCloudApplication
class SipToolsApplication

fun main(args: Array<String>) {
    SpringApplication.run(SipToolsApplication::class.java, *args)
}
