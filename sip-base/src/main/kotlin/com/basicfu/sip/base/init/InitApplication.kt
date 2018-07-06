package com.basicfu.sip.base.init

import com.basicfu.sip.base.service.ApplicationService
import com.basicfu.sip.core.Constant
import com.basicfu.sip.core.util.RedisUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

/**
 * @author basicfu
 * @date 2018/6/22
 */
@Order()
@Component
class InitApplication : CommandLineRunner {
    @Autowired
    lateinit var applicationService: ApplicationService

    override fun run(vararg args: String?) {
        val all = applicationService.all()
        val list = all.associateBy({ it.id.toString() }, { it })
        RedisUtil.hMSet(Constant.Redis.APP, list)
    }
}
