package com.basicfu.sip.dict.service

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class DictServiceTests {
    @Autowired
    lateinit var dictService: DictService

    @Test
    fun all() {

    }

    @Test
    fun list() {

    }

    @Test
    fun get() {
//        val result = dictService.get("dog1")
//        println(JSON.toJSONString(result))
    }

    @Test
    fun insert() {
//        val vo = DictVo()
//        vo.pid=2
//        vo.name="猫"
//        vo.value="dog2"
//        vo.description="(>^ω^<)喵"
//        val result = dictService.insert(vo)
//        Assert.assertEquals(result,1)
    }

    @Test
    fun update() {

    }

    @Test
    fun delete() {

    }
}
