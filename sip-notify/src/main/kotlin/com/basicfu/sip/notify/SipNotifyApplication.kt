package com.basicfu.sip.notify
import org.springframework.boot.SpringApplication
import org.springframework.cloud.client.SpringCloudApplication
import org.springframework.cloud.netflix.feign.EnableFeignClients
import tk.mybatis.spring.annotation.MapperScan

@MapperScan(basePackages = ["com.basicfu.sip.notify.mapper"])
@EnableFeignClients
@SpringCloudApplication
class SipNotifyApplication

fun main(args: Array<String>) {
    SpringApplication.run(SipNotifyApplication::class.java, *args)
}