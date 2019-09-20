package com.basicfu.sip.base.service

import com.basicfu.sip.base.BaseTests
import com.basicfu.sip.base.mapper.AppMapper
import com.basicfu.sip.base.model.po.App
import org.junit.Assert.assertEquals
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired


/**
 * @author basicfu
 * @date 2018/7/12
 */
class UserServiceTests : BaseTests<AppMapper, App>() {
    @Autowired
    lateinit var userService: UserService

    @Test
    fun registerByUsernamePassword() {
        val map= hashMapOf<String,String>()
        map["type"]="USERNAME_PASSWORD"
        map["username"]="basicfu"
        map["password"]="123456"
        assertEquals(1, userService.insert(map))
    }

//    @Test
//    fun insert() {
//        val vo = generate<AppVo> {
//            name = "无人机"
//            code = "hello"
//        }
//        assertEquals(1, appService.insert(vo))
//        try {
//            vo.name = "无人机1"
//            appService.insert(vo)
//            fail()
//        } catch (e: CustomException) {
//            assertTrue(true)
//        }
//        try {
//            vo.name = "无人机"
//            vo.code = "hello"
//            appService.insert(vo)
//            fail()
//        } catch (e: CustomException) {
//            assertTrue(true)
//        }
//        appService.delete(listOf(mapper.selectLastInsertId()))
//    }
//
//    @Test
//    fun update() {
//        val id = preInsert()
//        val vo = generate<AppVo> {
//            this.id=id
//            name = "无人机1"
//            code = "hello1"
//        }
//        assertEquals(1, appService.update(vo))
//        appService.delete(listOf(id))
//    }
//
//    @Test
//    fun delete() {
//        val id = preInsert()
//        assertEquals(1, appService.delete(listOf(id)))
//    }
}
