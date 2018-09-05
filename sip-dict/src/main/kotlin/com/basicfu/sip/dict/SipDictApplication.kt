package com.basicfu.sip.dict

import com.basicfu.sip.client.annotation.EnableSipClient
import com.basicfu.sip.client.common.Function
import com.basicfu.sip.core.annotation.EnableSipCore
import org.springframework.boot.SpringApplication
import org.springframework.cloud.client.SpringCloudApplication
import org.springframework.cloud.netflix.feign.EnableFeignClients
import tk.mybatis.spring.annotation.MapperScan

@MapperScan(basePackages = ["com.basicfu.sip.dict.mapper"])
@EnableFeignClients
@EnableSipClient(disable = [Function.Dict,Function.Logs])
@EnableSipCore
@SpringCloudApplication
class SipDictApplication

fun main(args: Array<String>) {
    SpringApplication.run(SipDictApplication::class.java, *args)
}
