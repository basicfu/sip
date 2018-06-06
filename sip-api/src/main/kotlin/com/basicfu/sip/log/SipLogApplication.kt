package com.basicfu.sip.log

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class SipLogApplication

fun main(args: Array<String>) {
    SpringApplication.run(SipLogApplication::class.java, *args)
}
