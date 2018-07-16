package com.basicfu.sip.permission.service

import com.basicfu.sip.core.common.mapper.generate
import com.basicfu.sip.permission.model.vo.RoleVo
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
class RoleServiceTests {
    @Autowired
    lateinit var roleService: RoleService

    @Test
    fun insert() {
        val vo=generate<RoleVo> {
            enalbe=1
            name="测试角色"
        }
        Assert.assertEquals(roleService.insert(vo),1)
    }
    @Test
    fun insertUser() {
        val vo=generate<RoleVo> {
            id=1
            userIds= arrayListOf(12)
        }
        Assert.assertEquals(roleService.insertUser(vo),1)
    }
    @Test
    fun insertMenu() {
        val vo=generate<RoleVo> {
            id=1
            menuIds= arrayListOf(1,2)
        }
        Assert.assertEquals(roleService.insertMenu(vo),2)
    }
    @Test
    fun insertPermission() {
        val vo=generate<RoleVo> {
            id=1
            permissionIds= arrayListOf(1)
        }
        Assert.assertEquals(roleService.insertPermission(vo),1)
    }
    @Test
    fun deleteUser() {
        val vo=generate<RoleVo> {
            id=1
            userIds= arrayListOf(12)
        }
        Assert.assertEquals(roleService.deleteUser(vo),1)
    }
    @Test
    fun deleteMenu() {
        val vo=generate<RoleVo> {
            id=1
            menuIds= arrayListOf(1,2)
        }
        Assert.assertEquals(roleService.deleteMenu(vo),2)
    }
    @Test
    fun deletePermission() {
        val vo=generate<RoleVo> {
            id=1
            permissionIds= arrayListOf(1)
        }
        Assert.assertEquals(roleService.deletePermission(vo),1)
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
