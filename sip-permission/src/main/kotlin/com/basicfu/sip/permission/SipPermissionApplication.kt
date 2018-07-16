package com.basicfu.sip.permission

import com.basicfu.sip.core.annotation.EnableSipCore
import org.springframework.boot.SpringApplication
import org.springframework.cloud.client.SpringCloudApplication
import org.springframework.cloud.netflix.feign.EnableFeignClients
import tk.mybatis.spring.annotation.MapperScan


@MapperScan(basePackages = ["com.basicfu.sip.permission.mapper"])
@EnableFeignClients
@EnableSipCore
@SpringCloudApplication
class SipPermissionApplication

fun main(args: Array<String>) {
    SpringApplication.run(SipPermissionApplication::class.java, *args)
}
