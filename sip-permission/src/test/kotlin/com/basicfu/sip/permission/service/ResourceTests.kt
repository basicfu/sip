package com.basicfu.sip.permission.service

import com.basicfu.sip.core.mapper.generate
import com.basicfu.sip.permission.model.vo.ResourceVo
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

/**
 * @author basicfu
 * @date 2018/7/11
 */
@RunWith(SpringRunner::class)
@SpringBootTest
class ResourceTests {
    @Autowired
    lateinit var resourceService:ResourceService

    @Test
    fun insert() {
        val vo = generate<ResourceVo> {
            serviceId=1
            url="/application/all"
            name="查询所有服务"
        }
        Assert.assertEquals(resourceService.insert(vo), 1)
    }

    @Test
    fun delete() {
//        userTemplateService.delete(listOf(3))
    }
}
