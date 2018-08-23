package com.basicfu.sip.logs

import com.basicfu.sip.core.annotation.EnableSipCore
import org.springframework.boot.SpringApplication
import org.springframework.cloud.client.SpringCloudApplication
import org.springframework.cloud.netflix.feign.EnableFeignClients
import tk.mybatis.spring.annotation.MapperScan

@MapperScan(basePackages = ["com.basicfu.sip.logs.mapper"])
@EnableFeignClients
@EnableSipCore
@SpringCloudApplication
class SipLogsApplication

fun main(args: Array<String>) {
    SpringApplication.run(SipLogsApplication::class.java, *args)
}
