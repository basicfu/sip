package com.basicfu.sip.base.service

import com.basicfu.sip.base.model.vo.ServiceVo
import com.basicfu.sip.core.common.mapper.generate
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

/**
 * @author basicfu
 * @date 2018/7/12
 */
@RunWith(SpringRunner::class)
@SpringBootTest
class ServiceServiceTests {
    @Autowired
    lateinit var serviceService: ServiceService

    @Test
    fun insert() {
        val vo = generate<ServiceVo> {
            appId = 1
            name = "sip-dict"
            path = "/dict/**"
            serverId = "sip-dict"
            stripPrefix = true
            retryable = true
        }
        Assert.assertEquals(serviceService.insert(vo), 1)
    }

    @Test
    fun delete() {
//        userTemplateService.delete(listOf(3))
    }
}
