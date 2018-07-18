package com.basicfu.sip.client.util

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.basicfu.sip.client.feign.RoleFeign
import com.basicfu.sip.client.feign.UserFeign
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Suppress("UNCHECKED_CAST")
@Component
class UserUtil {
    @Autowired
    private lateinit var userFeignTmp: UserFeign
    @Autowired
    private lateinit var roleFeignTmp: RoleFeign

    @PostConstruct
    fun init() {
        UserUtil.userFeign = userFeignTmp
        UserUtil.roleFeign = roleFeignTmp

    }

    companion object {
        @PublishedApi
        internal
        lateinit var userFeign: UserFeign

        @PublishedApi
        internal
        lateinit var roleFeign: RoleFeign


        /**
         * 获取当前登录用户信息
         */
        inline fun <reified T> getCurrentUser(): T? {
            return dealUser(userFeign.getCurrentUser().data)
        }

        /**
         * 根据用户Id获取用户信息
         */
        inline fun <reified T> get(id: Long): T? {
            val user = userFeign.get(id).data
            return dealUser(user)
        }

        /**
         * 根据用户Id获取用户信息
         */
        inline fun <reified T> listByIds(ids: List<Long>): List<T> {
            val user = userFeign.listByIds(ids).data
            return dealUser(user)
        }

        /**
         * 根据用户Ids获取用户名(返回Map<Id,Username>)
         */
        fun listUsernameByIds(ids: List<Long>): Map<Long, String> {
            return userFeign.listUsernameByIds(ids).data?.associateBy(
                { it.getLong("id") },
                { it.getString("username") }) ?: HashMap()
        }

        /**
         * 用户suggest
         */
        inline fun <reified T> suggest(name: String, limit: Int): List<T> {
            return dealUser(userFeign.suggest(name, limit).data)
        }

        /**
         * 根据用户id获取用户拥有的roles/menus/permissions/resources
         */
        inline fun <reified T> getPermissionByUid(uid: Long): T? {
            return dealUser(roleFeign.getPermissionByUid(uid).data)
        }

        /**
         * 根据用户id获取用户拥有的roles/menus/permissions/resources
         */
        fun getPermissionByUidJson(uid: Long): JSONObject? {
            return roleFeign.getPermissionByUid(uid).data
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
                    if (it.name == "roles" || it.name == "menus" || it.name == "permissions") {
                        it.isAccessible = true
                        it.set(u, tmpUser.getJSONArray(it.name))
                    } else if (it.name == "resources") {
                        it.isAccessible = true
                        it.set(u, tmpUser.getJSONObject(it.name) as Map<String, List<String>>)
                    }
                }
                result.add(u)
            }
            return result
        }
    }
}
