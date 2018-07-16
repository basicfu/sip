package com.basicfu.sip.dict

import com.basicfu.sip.core.annotation.EnableSipCore
import org.springframework.boot.SpringApplication
import org.springframework.cloud.client.SpringCloudApplication
import org.springframework.cloud.netflix.feign.EnableFeignClients
import tk.mybatis.spring.annotation.MapperScan

@MapperScan(basePackages = ["com.basicfu.sip.dict.mapper"])
@EnableFeignClients
@EnableSipCore
@SpringCloudApplication
class SipDictApplication

fun main(args: Array<String>) {
    SpringApplication.run(SipDictApplication::class.java, *args)
}
