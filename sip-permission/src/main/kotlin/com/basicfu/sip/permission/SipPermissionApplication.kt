package com.basicfu.sip.permission

import com.basicfu.sip.client.annotation.EnableSipClient
import com.basicfu.sip.core.annotation.EnableSipCore
import org.springframework.boot.runApplication
import org.springframework.cloud.client.SpringCloudApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import tk.mybatis.spring.annotation.MapperScan

@MapperScan(basePackages = ["com.basicfu.sip.permission.mapper"])
@EnableFeignClients
@EnableSipClient
@EnableSipCore
@SpringCloudApplication
class SipPermissionApplication

fun main(args: Array<String>) {
    runApplication<SipPermissionApplication>(*args)
}
