package com.basicfu.sip.logs.service

import com.basicfu.sip.core.common.mapper.generate
import com.basicfu.sip.core.util.RedisUtil
import com.basicfu.sip.logs.modal.vo.TemplateVo
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

/**
 * @author basicfu
 * @date 2018/8/23
 */
@RunWith(SpringRunner::class)
@SpringBootTest
class TemplateServiceTests {
    @Autowired
    lateinit var templateService: TemplateService

    @Test
    fun all() {
        val vo = generate<TemplateVo> {
            name = "登"
        }
        Assert.assertEquals(templateService.list(vo).size, 1)
    }

    @Test
    fun insert() {
        val vo = generate<TemplateVo> {
            name = "登录记录"
        }
        Assert.assertEquals(templateService.insert(vo), 1)
    }

    @Test
    fun insertField() {
        val vo = generate<TemplateVo> {
            id = 1
            name = "用户"
            enName = "user"
            sort = 1
        }
        Assert.assertEquals(templateService.insertField(vo), 1)
    }

    @Test
    fun update() {
        val vo = generate<TemplateVo> {
            id = 1
            name = "登录"
        }
        Assert.assertEquals(templateService.update(vo), 1)
    }

    @Test
    fun updateField() {
        val vo = generate<TemplateVo> {
            id = 1
            name = "用户字段"
            enName = "user"
            sort = 1
        }
        Assert.assertEquals(templateService.updateField(vo), 1)
    }

    @Test
    fun delete() {
        templateService.delete(listOf(1))
    }

    @Test
    fun deleteField() {
        templateService.deleteField(listOf(1))
    }
}
