package com.basicfu.sip.base.service

import com.basicfu.sip.base.BaseTests
import com.basicfu.sip.base.mapper.UserTemplateMapper
import com.basicfu.sip.base.model.po.UserTemplate
import com.basicfu.sip.base.model.vo.UserTemplateVo
import com.basicfu.sip.core.common.exception.CustomException
import com.basicfu.sip.core.common.mapper.generate
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired


/**
 * @author basicfu
 * @date 2018/6/30
 */
class UserTemplateServiceTests:BaseTests<UserTemplateMapper,UserTemplate>() {
    @Autowired
    lateinit var userTemplateService: UserTemplateService

    fun preInsert():Long{
        val vo=generate<UserTemplateVo> {
            name="姓名"
            enName="name"
            type="TEXT"
            extra="2~10"
            defaultValue="小明"
            required=true
            sort=1
        }
        assertEquals(1,userTemplateService.insert(vo))
        return mapper.selectLastInsertId()
    }

    @Test
    fun list() {
        val id=preInsert()
        val list = userTemplateService.list(generate {
            name = "姓"
        })
        assertEquals(1,list.size)
        userTemplateService.delete(listOf(id))
    }

    @Test
    fun all() {
        val id=preInsert()
        Assert.assertEquals(1,userTemplateService.all().size)
        userTemplateService.delete(listOf(id))
    }

    @Test
    fun insert() {
        val vo=generate<UserTemplateVo> {
            name="姓名"
            enName="name"
            type="TEXT"
            extra="2~10"
            defaultValue="小明"
            required=true
            sort=1
        }
        assertEquals(1,userTemplateService.insert(vo))
        try{
            vo.name="姓名1"
            userTemplateService.insert(vo)
            fail()
        }catch (e:CustomException){
            assertTrue(true)
        }
        try{
            vo.name="姓名"
            vo.enName="name1"
            userTemplateService.insert(vo)
            fail()
        }catch (e:CustomException){
            assertTrue(true)
        }
        userTemplateService.delete(listOf(mapper.selectLastInsertId()))
    }

    @Test
    fun update() {
        val id=preInsert()
        val vo=generate<UserTemplateVo> {
            this.id=id
            name="姓名1"
            enName="name1"
            type="NUMBER"
            extra="3~10"
            defaultValue="0"
            required=false
            sort=2
        }
        assertEquals(1,userTemplateService.update(vo))
        userTemplateService.delete(listOf(id))
    }

    @Test
    fun delete() {
        val id=preInsert()
        assertEquals(1,userTemplateService.delete(listOf(id)))
    }
}
