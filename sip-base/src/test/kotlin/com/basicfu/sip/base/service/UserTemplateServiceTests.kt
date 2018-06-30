package com.basicfu.sip.base.service

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

/**
 * @author basicfu
 * @date 2018/6/30
 */
@RunWith(SpringRunner::class)
@SpringBootTest
class UserTemplateServiceTests {
    @Autowired
    lateinit var userTemplateService: UserTemplateService

    @Test
    fun all() {
//        val vo=UserTemplateVo()
//        vo.name="昵"
//        Assert.assertEquals(userTemplateService.all(vo).size,1)
    }

    @Test
    fun insert() {
//        val vo=UserTemplateVo()
//        vo.name="昵称"
//        vo.type="Text"
//        vo.extra="5"
//        Assert.assertEquals(userTemplateService.insert(vo),1)
    }

    @Test
    fun update() {
//        val vo=UserTemplateVo()
//        vo.id=3
//        vo.name="昵称abc"
//        vo.type="Text"
//        vo.extra="5"
//        Assert.assertEquals(userTemplateService.update(vo),1)
    }

    @Test
    fun delete() {
//        userTemplateService.delete(listOf(3))
    }
}