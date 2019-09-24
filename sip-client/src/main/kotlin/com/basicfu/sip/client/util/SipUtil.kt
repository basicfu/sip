package com.basicfu.sip.client.util

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.basicfu.sip.client.model.PermissionResult
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
    }

    companion object {
        var APP = ""
        var LoginUsername = ""
        private var userUrl = ""
        private var checkPermissionUrl = ""
        private var loginUrl = ""
        private var logoutUrl = ""
        private var registerUrl = ""
        private var getUserUrl = ""

        /**
         * 获取当前登录信息
         * 开发环境下有伪登录
         */
        fun user(): UserResult {
            return if (LoginUsername.isNotBlank()) {
                val json =
                    JSON.parseObject(HttpUtil.get("$getUserUrl?username=$LoginUsername", APP)).getJSONObject("data")
                JSON.toJavaObject(json, UserResult::class.java) ?: throw RuntimeException("找不到用户名")
            } else {
                val json = JSON.parseObject(HttpUtil.get(userUrl, APP)).getJSONObject("data")
                val userResult = JSON.toJavaObject(json, UserResult::class.java)
                userResult.fullJson = json
                userResult
            }
        }

        /***
         * 检查用户是否拥有指定接口的权限
         */
        fun checkPermission(method: String, url: String): PermissionResult {
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
                ).getJSONObject("data"), PermissionResult::class.java
            )
        }

        /**
         * 根据用户id获取用户详情
         */
        fun getUserById() {

        }

        /**
         * 根据用户名获取用户详情
         */
        fun getUserByUsername() {

        }

        /**
         * 批量根据用户id获取用户详情
         */
        fun listUserByIds() {

        }

        /**
         * 批量根据用户名获取用户详情
         */
        fun listUserByUsernames() {

        }

        /**
         * 用户suggest
         */
        fun suggestUser() {

        }

        fun resetPassowrd() {

        }


        fun userController(): JSONObject {
            return if (LoginUsername.isNotBlank()) {
                JSON.parseObject(HttpUtil.get("$getUserUrl?username=$LoginUsername", APP))
            } else {
                JSON.parseObject(HttpUtil.get(userUrl, APP))
            }
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
    }
}
