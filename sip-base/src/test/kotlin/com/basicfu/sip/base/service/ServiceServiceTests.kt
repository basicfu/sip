package com.basicfu.sip.base.service

import com.basicfu.sip.base.BaseTests
import com.basicfu.sip.base.mapper.ServiceMapper
import com.basicfu.sip.base.model.vo.ServiceVo
import com.basicfu.sip.core.common.exception.CustomException
import com.basicfu.sip.core.common.mapper.generate
import com.basicfu.sip.core.model.po.Service
import org.junit.Assert
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author basicfu
 * @date 2018/7/12
 */
class ServiceServiceTests : BaseTests<ServiceMapper, Service>() {
    @Autowired
    lateinit var serviceService: ServiceService

    fun preInsert(): Long {
        val vo = generate<ServiceVo> {
            name = "基础服务"
            path = "/base/**"
            serverId="sip-base"
            url=""
            stripPrefix=true
            retryable=true
            sensitiveHeaders=""
        }
        Assert.assertEquals(1, serviceService.insert(vo))
        return mapper.selectLastInsertId()
    }

    @Test
    fun list() {
        val id = preInsert()
        val list = serviceService.all()
        Assert.assertEquals(1, list.size)
        serviceService.delete(listOf(id))
    }

    @Test
    fun insert() {
        val vo = generate<ServiceVo> {
            name = "基础服务"
            path = "/base/**"
            serverId="sip-base"
            url="https://www.baidu.com"
            stripPrefix=true
            retryable=true
            sensitiveHeaders=""
        }
        Assert.assertEquals(1, serviceService.insert(vo))
        try {
            vo.path = "/wbase/**"
            serviceService.insert(vo)
            Assert.fail()
        } catch (e: CustomException) {
            Assert.assertTrue(true)
        }
        try {
            vo.path = "/base/**"
            vo.serverId = "sip-base1"
            serviceService.insert(vo)
            Assert.fail()
        } catch (e: CustomException) {
            Assert.assertTrue(true)
        }
        try {
            vo.serverId = "sip-base"
            vo.url = "https://www.baidu.com1"
            serviceService.insert(vo)
            Assert.fail()
        } catch (e: CustomException) {
            Assert.assertTrue(true)
        }
        serviceService.delete(listOf(mapper.selectLastInsertId()))
    }

    @Test
    fun update() {
        val id = preInsert()
        val vo = generate<ServiceVo> {
            this.id=id
            name = "基础服务"
            path = "/base/**"
            serverId="sip-base"
            url="https://www.baidu.com"
            stripPrefix=true
            retryable=true
            sensitiveHeaders=""
        }
        Assert.assertEquals(1, serviceService.update(vo))
        serviceService.delete(listOf(id))
    }

    @Test
    fun delete() {
        val id = preInsert()
        Assert.assertEquals(1, serviceService.delete(listOf(id)))
    }
}
