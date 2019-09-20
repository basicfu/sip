package com.basicfu.sip.client.util

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct


/**
 * @author basicfu
 * @date 2019-07-04
 */

@Component
class ApiUtil {
    @Value("\${sip.host:}")
    final val host: String = ""
    @Value("\${sip.app:}")
    final val app: String = ""

    @PostConstruct
    fun init() {
        APP = app
        userUrl = "$host/user"
        checkPermissionUrl = "$host/sip/permission/check"
        loginUrl = "$host/user/login"
        logoutUrl = "$host/user/logout"
        registerUrl = "$host/user/register"
    }

    companion object {
        var APP = ""
        private var userUrl = ""
        private var checkPermissionUrl = ""
        private var loginUrl = ""
        private var logoutUrl = ""
        private var registerUrl = ""

        /**
         * 获取当前登录信息
         */
        fun getCurrentUser(): JSONObject {
            return JSON.parseObject(HttpUtil.get(userUrl, null, APP))
        }

        /***
         * 检查用户是否拥有指定接口的权限
         */
        fun checkPermission(method:String,url: String): JSONObject {
            val json = JSONObject()
            json["method"] = method
            json["url"] = url
            return JSON.parseObject(HttpUtil.post(checkPermissionUrl, json, APP))
        }

        /***
         * 登录
         */
        fun login(username: String, password: String): JSONObject {
            val json = JSONObject()
            json["username"] = username
            json["password"] = password
            return JSON.parseObject(HttpUtil.post(loginUrl, json, APP))
        }

        /**
         * 注册
         */
        fun register(map: Map<String, Any>): JSONObject {
            return JSON.parseObject(HttpUtil.post(registerUrl, map, APP))
        }

        /**
         * 退出
         */
        fun logout(): JSONObject {
            return JSON.parseObject(HttpUtil.get(logoutUrl, null, APP))
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
    }
}
