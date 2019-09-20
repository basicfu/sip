package com.basicfu.sip.base.init

import com.basicfu.sip.base.service.RoleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

/**
 * @author basicfu
 * @date 2018/7/12
 */
@Component
class InitConfig : CommandLineRunner {
    @Autowired
    lateinit var roleService: RoleService

    override fun run(vararg strings: String) {
        roleService.refreshRolePermission()
    }
}
