package com.basicfu.sip.core.util

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.serializer.SerializerFeature
import com.basicfu.sip.core.model.dto.UserDto

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
}
