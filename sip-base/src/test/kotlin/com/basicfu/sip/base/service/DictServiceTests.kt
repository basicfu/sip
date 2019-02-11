package com.basicfu.sip.base.service

import com.basicfu.sip.common.model.vo.DictVo
import org.junit.Assert
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
        val vo = DictVo()
        vo.pid = 1
        vo.name = "用户模板字段类型"
        vo.value = "USER_TEMPLATE_FIELD_TYPE"
        vo.description = ""
        val result = dictService.insert(vo)
        Assert.assertEquals(result, 1)
    }

    @Test
    fun update() {

    }

    @Test
    fun delete() {

    }
}
