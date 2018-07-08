package com.basicfu.sip.client.util

import com.alibaba.fastjson.JSON
import com.basicfu.sip.client.feign.UserFeign
import com.basicfu.sip.client.model.UserDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
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
         * 根据用户Id获取用户信息
         */
        inline fun <reified T> get(id: Long): T? {
            val user = userFeign.get(id).data
            return dealUser(user)
        }

        /**
         * 根据用户Ids获取用户名(返回Map<Id,Username>)
         */
        fun listUsernameByIds(ids: List<Long>): Map<Long, String> {
            return userFeign.listUsernameByIds(ids).data?.associateBy({ it.id!! }, { it.username!! })?:HashMap()
        }

        /**
         * 获取当前登录用户信息
         */
        inline fun <reified T> getCurrentUser():T?{
            val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
            val token= request.getHeader("token") ?: return null
            val user = userFeign.getByToken(token).data
            return dealUser(user)
        }

        /**
         * 服务器端用户对象转换为用户提供对象
         */
        @PublishedApi
        internal inline fun <reified T> dealUser(user:UserDto?):T?{
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
