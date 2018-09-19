package com.basicfu.sip.base.service

import com.basicfu.sip.base.BaseTests
import com.basicfu.sip.base.mapper.AppServiceMapper
import com.basicfu.sip.base.model.po.AppService
import com.basicfu.sip.base.model.vo.AppServiceVo
import com.basicfu.sip.core.common.exception.CustomException
import com.basicfu.sip.core.common.mapper.generate
import org.junit.Assert
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author basicfu
 * @date 2018/7/12
 */
class ServiceServiceTests : BaseTests<AppServiceMapper, AppService>() {
    @Autowired
    lateinit var appServiceService: AppServiceService

    fun preInsert(): Long {
        val vo = generate<AppServiceVo> {
            name = "基础服务"
            path = "/base/**"
            serverId = "sip-base"
            url = ""
            stripPrefix = true
            retryable = true
            sensitiveHeaders = ""
        }
        Assert.assertEquals(1, appServiceService.insert(vo))
        return mapper.selectLastInsertId()
    }

    @Test
    fun list() {
        val id = preInsert()
        val list = appServiceService.all()
        Assert.assertEquals(1, list.size)
        appServiceService.delete(listOf(id))
    }

    @Test
    fun insert() {
        val vo = generate<AppServiceVo> {
            name = "基础服务"
            path = "/base/**"
            serverId = "sip-base"
            url = "https://www.baidu.com"
            stripPrefix = true
            retryable = true
            sensitiveHeaders = ""
        }
        Assert.assertEquals(1, appServiceService.insert(vo))
        try {
            vo.path = "/wbase/**"
            appServiceService.insert(vo)
            Assert.fail()
        } catch (e: CustomException) {
            Assert.assertTrue(true)
        }
        try {
            vo.path = "/base/**"
            vo.serverId = "sip-base1"
            appServiceService.insert(vo)
            Assert.fail()
        } catch (e: CustomException) {
            Assert.assertTrue(true)
        }
        try {
            vo.serverId = "sip-base"
            vo.url = "https://www.baidu.com1"
            appServiceService.insert(vo)
            Assert.fail()
        } catch (e: CustomException) {
            Assert.assertTrue(true)
        }
        appServiceService.delete(listOf(mapper.selectLastInsertId()))
    }

    @Test
    fun update() {
        val id = preInsert()
        val vo = generate<AppServiceVo> {
            this.id = id
            name = "基础服务"
            path = "/base/**"
            serverId = "sip-base"
            url = "https://www.baidu.com"
            stripPrefix = true
            retryable = true
            sensitiveHeaders = ""
        }
        Assert.assertEquals(1, appServiceService.update(vo))
        appServiceService.delete(listOf(id))
    }

    @Test
    fun delete() {
        val id = preInsert()
        Assert.assertEquals(1, appServiceService.delete(listOf(id)))
    }
}
