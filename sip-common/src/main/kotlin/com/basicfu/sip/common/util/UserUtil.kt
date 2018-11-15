package com.basicfu.sip.common.util

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.serializer.SerializerFeature
import com.basicfu.sip.common.model.dto.UserDto
import org.springframework.util.ReflectionUtils

object UserUtil {
    /**
     * 用户转json返回
     */
    fun toJson(user: UserDto?): JSONObject? {
        val result = JSONObject()
        if (user != null) {
            user.content?.let { result.putAll(JSON.parseObject(it));user.content = null }
            result.putAll(JSON.parseObject(JSON.toJSONString(user, SerializerFeature.WriteMapNullValue)))

        }
        return result
    }

    fun toJson(users: List<UserDto>?): List<JSONObject>? {
        val result = arrayListOf<JSONObject>()
        users?.forEach { user ->
            val obj = JSONObject()
            user.content?.let { obj.putAll(JSON.parseObject(it));user.content = null }
            obj.putAll(JSON.parseObject(JSON.toJSONString(user, SerializerFeature.WriteMapNullValue)))
            obj.remove(user::content.name)
            result.add(obj)
        }
        return result
    }

    inline fun <reified T> toUser(map: Map<String, Any>): T {
        val content = JSONObject()
        val beanFields = T::class.java.declaredFields.map { it.name }
        map.keys.filter { !beanFields.contains(it) }.forEach {
            content[it] = map[it]
        }
        val user = JSON.toJavaObject(JSON.parseObject(JSON.toJSONString(map)), T::class.java)
        val field = ReflectionUtils.findField(T::class.java, "content")
        field?.let {
            it.isAccessible = true
            it.set(user, content)
        }
        return user
    }
}
