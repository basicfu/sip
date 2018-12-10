package com.basicfu.sip.dict

import com.basicfu.sip.client.annotation.EnableSipClient
import com.basicfu.sip.client.common.Function
import com.basicfu.sip.core.annotation.EnableSipCore
import org.springframework.boot.runApplication
import org.springframework.cloud.client.SpringCloudApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import tk.mybatis.spring.annotation.MapperScan

@MapperScan(basePackages = ["com.basicfu.sip.dict.mapper"])
@EnableFeignClients
@EnableSipClient(disable = [Function.Dict])
@EnableSipCore
@SpringCloudApplication
class SipDictApplication

fun main(args: Array<String>) {
    runApplication<SipDictApplication>(*args)
}
