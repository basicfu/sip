package com.basicfu.sip.getway.init

import com.basicfu.sip.core.Constant
import com.basicfu.sip.core.model.dto.UserDto
import com.basicfu.sip.core.util.RedisUtil
import com.basicfu.sip.getway.mapper.Mapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

/**
 * @author fuliang
 * @date 2018/7/12
 */
@Component("getwayInitConfig")
class InitConfig : CommandLineRunner {
    @Autowired
    lateinit var mapper: Mapper

    override fun run(vararg strings: String) {
        initResource()
        initNoLoginToken()
        //TODO 初始化app和app下的service
    }

    /**
     * 初始化未登录用户token永不过期
     */
    fun initNoLoginToken() {
//        val user = UserDto()
//        user.resourceIds
//        val menuResourceIds = mapper.selectMenuResourceIdByRoleCode(Constant.System.GUEST)
//        val permissionResourceIds = mapper.selectPermissionResourceIdByRoleCode(Constant.System.GUEST)
//        val resourceIds = arrayListOf<Long>()
//        resourceIds.addAll(menuResourceIds)
//        resourceIds.addAll(permissionResourceIds)
//        user.resourceIds = resourceIds.distinct()
//        RedisUtil.set(Constant.Redis.TOKEN_GUEST, user)
    }

    fun initResource() {
        val resources = mapper.selectResource()
        RedisUtil.del(Constant.Redis.RESOURCE)
        RedisUtil.hMSet(
            Constant.Redis.RESOURCE,
            resources.associateBy({ it.serviceId.toString() + "|" + it.url }, { it.id })
        )
    }

}
