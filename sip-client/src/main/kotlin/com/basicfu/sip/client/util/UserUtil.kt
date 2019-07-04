package com.basicfu.sip.client.util

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.basicfu.sip.client.feign.BaseFeign
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Suppress("UNCHECKED_CAST")
@Component
class UserUtil {
    @Autowired
    private lateinit var baseFeignTmp: BaseFeign

    @PostConstruct
    fun init() {
        UserUtil.baseFeign = baseFeignTmp

    }

    companion object {
        @PublishedApi
        internal
        lateinit var baseFeign: BaseFeign

        /**
         * 获取当前登录用户信息
         */
        inline fun <reified T> getCurrentUser(): T? {
            return dealUser(baseFeign.getCurrentUser().data)
        }

        /**
         * 根据用户Id获取用户信息
         */
        inline fun <reified T> getUserById(id: Long): T? {
            val user = baseFeign.get(id,null).data
            return dealUser(user)
        }

        /**
         * 根据用户名获取用户信息
         */
        inline fun <reified T> getUserByUsername(username: String): T? {
            val user = baseFeign.get(null,username).data
            return dealUser(user)
        }

        /**
         * 根据用户Id获取用户信息
         */
        inline fun <reified T> listByIds(ids: List<Long>): List<T> {
            val user = baseFeign.listByIds(ids.toTypedArray()).data
            return dealUser(user)
        }

        /**
         * 根据用户Ids获取用户名(返回Map<Id,Username>)
         */
        fun listUsernameByIds(ids: List<Long>): Map<Long, String> {
            return baseFeign.listUsernameByIds(ids.toTypedArray()).data?.associateBy(
                { it.getLong("id") },
                { it.getString("username") }) ?: HashMap()
        }

        /**
         * 用户suggest
         */
        inline fun <reified T> suggest(name: String, limit: Int): List<T> {
            return dealUser(baseFeign.suggest(name, null, limit).data)
        }

        /**
         * 用户suggest并根据roleCode
         */
        inline fun <reified T> suggestByRoleCode(name: String, roleCode: String, limit: Int): List<T> {
            return dealUser(baseFeign.suggest(name, roleCode, limit).data)
        }

        /**
         * 根据用户id获取用户拥有的roleCode
         */
        fun listRoleCodeByUid(uid: Long): List<String> {
            return baseFeign.listRoleByUid(uid).data!!
        }

        /**
         * 根据用户ID查询用户角色
         */
        fun listRoleByIds(userIds: List<Long>): Map<Long, List<String>> {
            if (userIds.isEmpty()) {
                return hashMapOf()
            }
            val listRoleByIds = baseFeign.listRoleByIds(userIds.toTypedArray()).data
            return listRoleByIds?.associateBy(
                { it.id!! },
                { it.roles!! }) ?: HashMap()
        }

        /**
         * 服务器端用户对象转换为用户提供对象
         */
        @PublishedApi
        internal inline fun <reified T> dealUser(user: JSONObject?): T? {
            if (user == null) {
                return null
            }
            val clazz = T::class.java
            val fields = clazz.declaredFields
            val tmpUser = JSON.parseObject(user.toJSONString())
            user.remove("roles")
            user.remove("menus")
            user.remove("permissions")
            user.remove("resources")
            val result = user.toJavaObject(clazz)
            fields.forEach { it ->
                if (it.name == "roles" || it.name == "menus" || it.name == "permissions") {
                    it.isAccessible = true
                    it.set(result, tmpUser.getJSONArray(it.name))
                } else if (it.name == "resources") {
                    it.isAccessible = true
                    it.set(result, tmpUser.getJSONObject(it.name) as Map<String, List<String>>)
                }
            }
            return result
        }

        @PublishedApi
        internal inline fun <reified T> dealUser(users: List<JSONObject>?): List<T> {
            val result = arrayListOf<T>()
            if (users == null) {
                return result
            }
            val clazz = T::class.java
            val fields = clazz.declaredFields
            users.forEach { user ->
                val tmpUser = JSON.parseObject(user.toJSONString())
                user.remove("roles")
                user.remove("menus")
                user.remove("permissions")
                user.remove("resources")
                val u = user.toJavaObject(clazz)
                fields.forEach { it ->
                    if (it.name == "roles"||it.name == "menus" ) {
                        it.isAccessible = true
                        it.set(u, tmpUser.getJSONObject(it.name))
                    }else if (it.name == "permissions") {
                        it.isAccessible = true
                        it.set(u, tmpUser.getJSONArray(it.name))
                    } else if (it.name == "resources") {
                        it.isAccessible = true
                        val obj = tmpUser.getJSONObject(it.name)
                        if (obj != null) {
                            it.set(u, tmpUser.getJSONObject(it.name) as Map<String, List<String>>)
                        }
                    }
                }
                result.add(u)
            }
            return result
        }
    }
}
