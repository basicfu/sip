package com.basicfu.sip.base.util

import com.basicfu.sip.base.common.constant.Constant
import com.basicfu.sip.base.model.dto.UserDto
import com.basicfu.sip.core.util.AESUtil
import com.basicfu.sip.core.util.RedisUtil
import com.basicfu.sip.core.util.RequestUtil
import com.google.gson.Gson
import java.util.*

object TokenUtil {

    /**
     * 获取redis中用户token前缀
     */
    fun getRedisUserTokenPrefix(username: String): String {
        return "${Constant.Redis.TOKEN_PREFIX}$username"
    }

    /**
     * 生成token
     */
    fun generateToken(username: String): String {
        return "${username}_${generateToken()}"
    }
    /**
     * 生成redis token
     */
    fun generateRedisToken(token: String): String {
        return "${Constant.Redis.TOKEN_PREFIX}$token"
    }

    /**
     * token aes加密给前台
     */
    fun generateFrontToken(token: String): String? {
        return AESUtil.encrypt(token, Constant.System.AES_TOKEN_KEY)
    }

    /**
     * 获取从前台传来的token并转为redis token
     */
    fun getCurrentRedisToken(): String? {
        return RequestUtil.getHeader(Constant.System.AUTHORIZATION)?.let {frontToken->
            AESUtil.decrypt(frontToken, Constant.System.AES_TOKEN_KEY)?.let { "${Constant.Redis.TOKEN_PREFIX}$it" }
        }
    }

    /**
     * 生成token
     */
    private fun generateToken(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }

    /**
     * 获取当前用户
     */
    fun getCurrentUser(): UserDto? {
        return getCurrentRedisToken()?.let {
            RedisUtil.get<String>(it)?.let {
                Gson().fromJson(it,UserDto::class.java)
//                JSON.parseObject(it).toJavaObject(UserDto::class.java)
            }
        }
    }
}