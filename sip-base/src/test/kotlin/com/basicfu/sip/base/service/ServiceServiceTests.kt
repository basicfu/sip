package com.basicfu.sip.base.service

import com.basicfu.sip.base.model.vo.AppVo
import com.basicfu.sip.base.model.vo.UserTemplateVo
import com.basicfu.sip.core.mapper.generate
import org.bouncycastle.asn1.x500.style.RFC4519Style.name
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

/**
 * @author basicfu
 * @date 2018/7/12
 */
@RunWith(SpringRunner::class)
@SpringBootTest
class AppServiceTests {
    @Autowired
    lateinit var appService: AppService

    @Test
    fun insert() {
        val vo=generate<AppVo> {
            name="梧桐招聘"
            domain="wutong"
        }
        Assert.assertEquals(appService.insert(vo),1)
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
