package com.basicfu.sip.base.service

import com.basicfu.sip.base.model.vo.PermissionVo
import com.basicfu.sip.core.common.mapper.generate
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

/**
 * @author basicfu
 * @date 2018/7/10
 */
@RunWith(SpringRunner::class)
@SpringBootTest
class PermissionServiceTests {
    @Autowired
    lateinit var permissionService: PermissionService

    @Test
    fun insert() {
        val vo = generate<PermissionVo> {
            name = "测试权限"
        }
        Assert.assertEquals(permissionService.insert(vo), 1)
    }

    @Test
    fun insertResource() {
        val vo = generate<PermissionVo> {
            id = 1
            resourceIds = arrayListOf(1,2)
        }
        Assert.assertEquals(permissionService.insertResource(vo), 2)
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
