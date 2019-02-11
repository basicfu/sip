package com.basicfu.sip.base.service

import com.basicfu.sip.base.model.vo.MenuVo
import com.basicfu.sip.core.common.mapper.generate
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

/**
 * @author basicfu
 * @date 2018/7/9
 */
@RunWith(SpringRunner::class)
@SpringBootTest
class MenuServiceTests {
    @Autowired
    lateinit var menuService: MenuService

    @Test
    fun insert() {
        val vo = generate<MenuVo> {
            pid=0
            name = "菜单管理"
            path="/menu"
            sort=1
            icon="menu"
            type="PAGE"
            display=true
        }
        Assert.assertEquals(menuService.insert(vo), 1)
    }

    @Test
    fun insertResource() {
        val vo = generate<MenuVo> {
            id=1
            resourceIds= arrayListOf(1)
        }
        Assert.assertEquals(menuService.insertResource(vo), 1)
    }
    @Test
    fun deleteResource() {
        val vo = generate<MenuVo> {
            id=1
            resourceIds= arrayListOf(1)
        }
        Assert.assertEquals(menuService.deleteResource(vo), 1)
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
