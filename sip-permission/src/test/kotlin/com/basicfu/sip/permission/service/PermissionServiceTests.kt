package com.basicfu.sip.permission.service

import com.basicfu.sip.core.mapper.generate
import com.basicfu.sip.permission.model.vo.MenuVo
import com.basicfu.sip.permission.model.vo.PermissionVo
import org.aspectj.weaver.tools.cache.SimpleCacheFactory.path
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
    fun insertMenu() {
//        val vo = generate<RoleVo> {
//            enalbe = 1
//            name = "测试角色"
//        }
//        Assert.assertEquals(menuService.insertMenu(vo), 1)
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