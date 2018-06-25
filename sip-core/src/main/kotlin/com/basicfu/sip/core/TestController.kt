package com.basicfu.sip.core

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author basicfu
 * @date 2018/6/22
 */
@RestController
@RequestMapping("/test")
class TestController{
    @GetMapping("/test")
    fun test(){

    }
}
