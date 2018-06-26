package com.basicfu.sip.client

import com.basicfu.sip.core.model.Result
import com.basicfu.sip.core.model.dto.DictDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.netflix.feign.FeignClient
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Service
class Test {
    @Autowired
    lateinit var dictFeign: DictFeign

    fun test(){
        val data = dictFeign.get("1").data
        data?.forEach { item->

        }
    }
}
