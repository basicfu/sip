package com.basicfu.sip.notify
import com.basicfu.sip.core.annotation.EnableSipCore
import org.springframework.boot.SpringApplication
import org.springframework.cloud.client.SpringCloudApplication
import org.springframework.cloud.netflix.feign.EnableFeignClients
import tk.mybatis.spring.annotation.MapperScan

@MapperScan(basePackages = ["com.basicfu.sip.notify.mapper"])
@EnableFeignClients
@EnableSipCore
@SpringCloudApplication
//@ComponentScan(basePackages = arrayOf("com.basicfu.sip"))
class SipNotifyApplication

fun main(args: Array<String>) {
    SpringApplication.run(SipNotifyApplication::class.java, *args)
}