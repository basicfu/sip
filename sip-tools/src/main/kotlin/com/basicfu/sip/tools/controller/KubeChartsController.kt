package com.basicfu.sip.tools.controller

import com.basicfu.sip.tools.service.KubeChartsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


/**
 * @author basicfu
 * @date 2018/8/15
 */
@RestController
@RequestMapping("/kube")
class KubeChartsController {
    @Autowired
    lateinit var kubeChartsService:KubeChartsService

    @PostMapping
    fun install(name:String,set:Array<String>?): String {
      return kubeChartsService.install(name,set).toJSONString()+"\n"
    }

}
