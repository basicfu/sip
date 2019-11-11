package com.basicfu.sip.client.util

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.basicfu.sip.client.model.PermissionResult
import com.basicfu.sip.client.model.Result
import com.basicfu.sip.client.model.UserResult
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct


/**
 * @author basicfu
 * @date 2019-07-04
 */

@Component
class SipUtil {
    @Value("\${sip.host:}")
    final val host: String = ""
    @Value("\${sip.app:}")
    final val app: String = ""
    @Value("\${sip.login:}")
    final val loginUsername: String = ""

    @PostConstruct
    fun init() {
        APP = app
        LoginUsername = loginUsername
        userUrl = "$host/user"
        checkPermissionUrl = "$host/sip/permission/check"
        loginUrl = "$host/user/login"
        logoutUrl = "$host/user/logout"
        registerUrl = "$host/user/register"
        getUserUrl = "$host/user/get"
        updateUserUrl = "$host/user/update"
        deleteUserUrl = "$host/user/delete"
        deletePhyUserUrl = "$host/user/delete/phy"
        updatePasswordUrl = "$host/user/update/password"
        findPasswordUrl = "$host/user/find/password"
        allRoleUrl = "$host/role/all"
        smsUrl = "$host/sip/sms"
        if(loginUsername.isNotBlank()){
            userResult = getUserByUsername(loginUsername)
        }
    }

    companion object {
        var APP = ""
        var LoginUsername = ""
        var userUrl = ""
        var checkPermissionUrl = ""
        var loginUrl = ""
        var logoutUrl = ""
        var registerUrl = ""
        var getUserUrl = ""
        var updateUserUrl = ""
        var deleteUserUrl = ""
        var deletePhyUserUrl = ""
        var updatePasswordUrl = ""
        var findPasswordUrl = ""
        var allRoleUrl = ""
        var smsUrl = ""
        var userResult: UserResult? = null

        /**
         * 获取当前登录信息
         * 开发环境下有伪登录，忽视是否有接口权限，且任何接口请求
         * 项目启动检查伪登录缓存用户信息、
         */
        fun user(): UserResult {
            return if (LoginUsername.isNotBlank()) {
                userResult!!
            } else {
                val json = JSON.parseObject(HttpUtil.get(userUrl, APP)).getJSONObject("data")
                JSON.toJavaObject(json, UserResult::class.java)
            }
        }

        /***
         * 检查用户是否拥有指定接口的权限
         */
        fun checkPermission(method: String, url: String): PermissionResult {
            if (LoginUsername.isNotBlank()) {
                val pr = PermissionResult()
                pr.success = true
                return pr
            } else {
                val json = JSONObject()
                json["method"] = method
                json["url"] = url
                return JSON.toJavaObject(
                    JSON.parseObject(
                        HttpUtil.post(
                            checkPermissionUrl,
                            json,
                            APP
                        )
                    ), PermissionResult::class.java
                )
            }
        }

        /**
         * 根据用户id获取用户详情
         */
        fun getUserById(id: String): UserResult {
            val json = JSON.parseObject(HttpUtil.get("$getUserUrl?id=$id", APP)).getJSONObject("data")
            return JSON.toJavaObject(json, UserResult::class.java) ?: throw RuntimeException("找不到用户ID")
        }

        /**
         * 根据用户名获取用户详情
         */
        fun getUserByUsername(username: String): UserResult {
            val json =
                JSON.parseObject(HttpUtil.get("$getUserUrl?username=$username", APP)).getJSONObject("data")
            return JSON.toJavaObject(json, UserResult::class.java) ?: throw RuntimeException("找不到用户名")
        }

        /**
         * 添加用户
         */
        fun insertUser(json: JSONObject): Result<*> {
            return JSON.parseObject(HttpUtil.post(registerUrl, json, APP))
                .toJavaObject(Result::class.java)
        }

        /**
         * 根据用户id更新用户信息
         */
        fun updateUser(json: JSONObject): Result<*> {
            return JSON.parseObject(HttpUtil.post(updateUserUrl, json, APP))
                .toJavaObject(Result::class.java)
        }

        /**
         * 根据原密码修改当前登录用户密码
         */
        fun updatePassword(originPassword: String, password: String): Result<*> {
            val json = JSONObject()
            json["originPassword"] = originPassword
            json["password"] = password
            return JSON.parseObject(HttpUtil.post(updatePasswordUrl, json, APP))
                .toJavaObject(Result::class.java)
        }

//        /**
//         * 找回用户密码
//         */
//        fun updatePassword(originPassword: String, password: String): Result<*> {
//            val json = JSONObject()
//            json["originPassword"] = originPassword
//            json["password"] = password
//            return JSON.parseObject(HttpUtil.post(updatePasswordUrl, json, APP))
//                .toJavaObject(Result::class.java)
//        }

        /**
         * 逻辑删除用户
         */
        fun deleteUser(ids: List<String>): Result<*> {
            return JSON.parseObject(HttpUtil.delete(deleteUserUrl, JSONArray(ids), APP))
                .toJavaObject(Result::class.java)
        }

        /**
         * 物理删除用户
         */
        fun deletePhyUser(ids: List<String>): Result<*> {
            return JSON.parseObject(HttpUtil.delete(deletePhyUserUrl, JSONArray(ids), APP))
                .toJavaObject(Result::class.java)
        }

//        /**
//         * 用户suggest
//         */
//        fun suggestUser() {
//
//        }

        fun userController(): JSONObject {
            return if (LoginUsername.isNotBlank()) {
                JSON.parseObject(HttpUtil.get("$getUserUrl?username=$LoginUsername&type=full", APP))
            } else {
                JSON.parseObject(HttpUtil.get("$userUrl?type=full", APP))
            }
        }

        fun smsController(mobile: String): JSONObject {
            return JSON.parseObject(HttpUtil.post("$smsUrl/$mobile",JSONObject(), APP))
        }

        fun loginController(map: Map<String, Any>): JSONObject {
            return JSON.parseObject(HttpUtil.post(loginUrl, map, APP))
        }

        fun registerController(map: Map<String, Any>): JSONObject {
            return JSON.parseObject(HttpUtil.post(registerUrl, map, APP))
        }

        fun logoutController(): JSONObject {
            return JSON.parseObject(HttpUtil.get(logoutUrl, APP))
        }

        fun updatePasswordController(map: Map<String, Any>): JSONObject {
            return JSON.parseObject(HttpUtil.post(updatePasswordUrl, map, APP))
        }

        fun findPasswordController(map: Map<String, Any>): JSONObject {
            return JSON.parseObject(HttpUtil.post(findPasswordUrl, map, APP))
        }

        fun updateUserController(map: Map<String, Any>): JSONObject {
            return JSON.parseObject(HttpUtil.post(updateUserUrl, map, APP))
        }

        fun deleteUserController(ids: List<String>): JSONObject {
            return JSON.parseObject(HttpUtil.delete(updateUserUrl, ids, APP))
        }

        fun deleteUserPhyController(ids: List<String>): JSONObject {
            return JSON.parseObject(HttpUtil.delete(updateUserUrl, ids, APP))
        }

        fun allRoleController(): JSONObject {
            return JSON.parseObject(HttpUtil.get(allRoleUrl, APP))
        }
    }
}
