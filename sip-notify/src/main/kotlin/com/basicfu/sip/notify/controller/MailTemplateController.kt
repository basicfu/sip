package com.basicfu.sip.notify.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author tianhu
 * @date 2018/7/18
 */
@RestController
@RequestMapping("/template")
class MailTemplateController{


    @GetMapping("/test")
    fun get(){
        println("hello")
    }
}