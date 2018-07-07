package com.basicfu.sip.client.util

import com.alibaba.fastjson.JSON
import com.basicfu.sip.client.feign.UserFeign
import com.basicfu.sip.client.model.UserDto
import org.apache.catalina.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class UserUtil {
    @Autowired
    private lateinit var userFeignTmp: UserFeign

    @PostConstruct
    fun init() {
        UserUtil.userFeign = userFeignTmp
    }

    companion object {
        @PublishedApi
        internal
        lateinit var userFeign: UserFeign

        /**
         * 根据用户ID获取用户信息
         */
        inline fun <reified T> get(id: Long): T? {
            val user = userFeign.get(id).data
            val clazz = T::class.java
            val result = clazz.newInstance()
            if (user == null) {
                return null
            }
            val contentJson = JSON.parseObject(user.content)
            val fields = clazz.declaredFields
            fields.forEach { it ->
                it.isAccessible = true
                when (it.name) {
                    "id" -> it.set(result, user.id)
                    "username" -> it.set(result, user.username)
                    "cdate" -> it.set(result, user.cdate)
                    "udate" -> it.set(result, user.udate)
                    "ldate" -> it.set(result, user.ldate)
                    "status" -> it.set(result, user.status)
                    else -> {
                        val value = contentJson[it.name]
                        if (value != null) {
                            it.set(result, value)
                        }
                    }
                }
            }
            return result
        }
    }
}
