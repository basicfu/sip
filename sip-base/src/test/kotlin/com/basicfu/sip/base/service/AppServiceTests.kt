package com.basicfu.sip.base.service

import com.basicfu.sip.base.BaseTests
import com.basicfu.sip.base.mapper.AppMapper
import com.basicfu.sip.base.model.po.App
import com.basicfu.sip.base.model.vo.AppVo
import com.basicfu.sip.core.common.exception.CustomException
import com.basicfu.sip.core.common.mapper.generate
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired


/**
 * @author basicfu
 * @date 2018/7/12
 */
class AppServiceTests : BaseTests<AppMapper, App>() {
    @Autowired
    lateinit var appService: AppService

    fun preInsert(): Long {
        val vo = generate<AppVo> {
            name = "无人机"
            code = "hello"
        }
        Assert.assertEquals(1, appService.insert(vo))
        return mapper.selectLastInsertId()
    }

    @Test
    fun list() {
        val id = preInsert()
        val list = appService.list(generate {
            name = "无人"
        })
        assertEquals(1, list.size)
        appService.delete(listOf(id))
    }

    @Test
    fun insert() {
        val vo = generate<AppVo> {
            name = "无人机"
            code = "hello"
        }
        assertEquals(1, appService.insert(vo))
        try {
            vo.name = "无人机1"
            appService.insert(vo)
            fail()
        } catch (e: CustomException) {
            assertTrue(true)
        }
        try {
            vo.name = "无人机"
            vo.code = "hello"
            appService.insert(vo)
            fail()
        } catch (e: CustomException) {
            assertTrue(true)
        }
        appService.delete(listOf(mapper.selectLastInsertId()))
    }

    @Test
    fun update() {
        val id = preInsert()
        val vo = generate<AppVo> {
            this.id=id
            name = "无人机1"
            code = "hello1"
        }
        assertEquals(1, appService.update(vo))
        appService.delete(listOf(id))
    }

    @Test
    fun delete() {
        val id = preInsert()
        assertEquals(1, appService.delete(listOf(id)))
    }
}
