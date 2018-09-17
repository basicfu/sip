package com.basicfu.sip.core.util

import com.basicfu.sip.core.common.Constant
import com.basicfu.sip.core.model.dto.UserDto
import java.util.*

object TokenUtil {

    /**
     * 获取token前缀
     */
    fun getRedisTokenPrefix(): String {
        return Constant.Redis.TOKEN_PREFIX
    }

    /**
     * 获取用户token前缀
     */
    fun getUserTokenPrefix(username: String): String {
        return "${AppUtil.getAppId()}_${username}_"
    }

    /**
     * 生成用户token
     */
    fun generateUserToken(username: String): String {
        return getUserTokenPrefix(username)+ generateToken()
    }

    /**
     * 获取redis中用户token前缀
     */
    fun getRedisUserTokenPrefix(username: String): String {
        return getRedisTokenPrefix()+ getUserTokenPrefix(username)
    }

    /**
     * 获取redis中完整的token
     */
    fun getRedisToken(userToken: String?=null): String {
        return getRedisTokenPrefix()+ userToken
    }

    /**
     * token转为前台使用的token
     */
    fun generateFrontToken(token: String): String? {
        return AESUtil.encrypt(token, Constant.System.AES_TOKEN_KEY)
    }

    /**
     * 获取前台token
     */
    fun getFrontToken(): String? {
        return RequestUtil.getHeader(Constant.System.AUTHORIZATION) ?: return null
    }

    /**
     * 获取实际token
     */
    fun getCurrentToken(frontToken:String?=null): String? {
        return if(frontToken==null){
            getFrontToken()?.let {
                AESUtil.decrypt(it, Constant.System.AES_TOKEN_KEY)?.let { "${Constant.Redis.TOKEN_PREFIX}$it" }
            }
        }else{
            AESUtil.decrypt(frontToken, Constant.System.AES_TOKEN_KEY)?.let { "${Constant.Redis.TOKEN_PREFIX}$it" }
        }
    }

    /**
     * 获取访客对象
     */
    fun getGuestUser(appId:Long?=null): UserDto? {
        return RedisUtil.get<UserDto>("${Constant.Redis.TOKEN_GUEST}${appId?:AppUtil.getAppId()}")
    }

    /**
     * 获取当前用户
     */
    fun getCurrentUser(): UserDto? {
        return getCurrentToken()?.let { RedisUtil.get<UserDto>(it) }
    }

    /**
     * 根据前台token获取用户
     */
    fun getCurrentUserByFrontToken(frontToken:String?=null): UserDto? {
        return getCurrentToken(frontToken)?.let { RedisUtil.get<UserDto>(it) }
    }

    /**
     * 生成token
     */
    private fun generateToken(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }

}
