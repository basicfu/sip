package com.basicfu.sip.getway.init

import com.basicfu.sip.core.common.Constant
import com.basicfu.sip.core.model.dto.UserDto
import com.basicfu.sip.core.model.po.Resource
import com.basicfu.sip.core.util.RedisUtil
import com.basicfu.sip.getway.mapper.Mapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

/**
 * @author basicfu
 * @date 2018/7/12
 */
@Component("getwayInitConfig")
class InitConfig : CommandLineRunner {
    @Autowired
    lateinit var mapper: Mapper

    override fun run(vararg strings: String) {
        initNoLoginToken()
        //TODO 初始化app和app下的service
    }

    /**
     * 初始化未登录用户token永不过期
     */
    fun initNoLoginToken() {
        val user = UserDto()
        val menuResources = mapper.selectMenuResourceIdByRoleCode(Constant.System.GUEST)
        val permissionResources = mapper.selectPermissionResourceIdByRoleCode(Constant.System.GUEST)
        val list = arrayListOf<Resource>()
        list.addAll(menuResources)
        list.addAll(permissionResources)
        user.resources = list.distinct().groupBy({ it.serviceId!! }, { "/" + it.method + it.url })
        RedisUtil.set(Constant.Redis.TOKEN_GUEST, user)
    }
}
