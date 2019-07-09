package com.basicfu.sip.base.service

import com.alibaba.fastjson.JSON
import com.basicfu.sip.base.common.constant.Constant
import com.basicfu.sip.base.common.enum.Enum
import com.basicfu.sip.base.model.po.User
import com.basicfu.sip.base.model.vo.UserVo
import com.basicfu.sip.base.util.MongoUtil
import com.basicfu.sip.base.util.PasswordUtil
import com.basicfu.sip.base.util.TokenUtil
import com.basicfu.sip.core.common.exception.CustomException
import com.basicfu.sip.core.common.mapper.generate
import com.basicfu.sip.core.util.RedisUtil
import com.basicfu.sip.core.util.log
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Updates.combine
import com.mongodb.client.model.Updates.set
import org.bson.Document
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

/**
 * @author basicfu
 * @date 2018/6/30
 */
@Service
class UserService {
    fun status() {
        var user = TokenUtil.getCurrentUser()
    }

    /**
     * 登录
     * 用户名登录
     * 后期密码使用加密后的值
     * TODO 界面配置登录模式
     * 模式一：系统配置允许用户同时在线用户数,如3，超过次数需等待其他用户过期或主动退出，否则只能修改密码强制退出所有用户
     * 模式二：每次登录后上次用户自动过期，登录后清除该用户其他token
     * 模式三：无限制登录次数
     * TODO 校验手机和邮箱拥有者后可开启手机或邮箱登录
     * 子账号使用@形式待确定
     *
     */
    fun login(vo: UserVo): Map<String, Any> {
        val username = vo.username!!
        val collection = MongoUtil.collection
        val userDocument = collection.find(eq("username", username)).first()
        if (userDocument == null) {
            log.warn("用户名不存在username:$username")
            throw CustomException("用户名或密码错误")
        }
        val password = userDocument.getString("password")
        if (!PasswordUtil.matches(vo.password!!, password!!)) {
            log.warn("用户名密码错误username:$username")
            throw CustomException("用户名或密码错误")
        }
        val loginModal = 3
        @Suppress("ConstantConditionIf")
        if (loginModal == 1) {
            val keys = RedisUtil.keys(TokenUtil.getRedisUserTokenPrefix(username) + "*")
            if (keys.size >= 2) {
                throw CustomException(Enum.THEN_USER_MAX_ONLINE)
            }
        } else if (loginModal == 2) {
            val keys = RedisUtil.keys(TokenUtil.getRedisUserTokenPrefix(username) + "*")
            RedisUtil.del(keys.map { it })
        }
        //TODO 记录用户登录记录
        //TODO 系统设置登录过期时间
        val token = TokenUtil.generateToken(username)
        RedisUtil.set(TokenUtil.generateRedisToken(token), userDocument.toJson(), Constant.System.SESSION_TIMEOUT)
        userDocument["_id"] = userDocument.getObjectId("_id").toHexString()
        userDocument["token"] = TokenUtil.generateFrontToken(token) ?: throw CustomException(Enum.LOGIN_ERROR)
        userDocument.remove("password")
        return userDocument
    }

    /**
     * 登出
     * TODO 如果是模式2需要移除所有token
     */
    fun logout() {
        TokenUtil.getCurrentRedisToken()?.let {
            RedisUtil.del(it)
        }
    }

    /**
     * 注册方式
     * 用户名密码
     * 用户名密码+手机号验证
     * 用户名密码+邮箱验证
     * 后台创建
     */
    fun insert(map: Map<String, Any>): Int {
        val user = generate<User> {
            username = map["username"].toString()
            nickname = map["nickname"].toString()
            mobile = map["mobile"].toString()
            email = map["email"].toString()
            password = BCryptPasswordEncoder().encode(map["password"].toString())
            mobileVerified = false
            emailVerified = false
            createTime = System.currentTimeMillis()
            updateTime = createTime
            blocked = false
            isdel = false
        }
        if (MongoUtil.collection.countDocuments(Filters.eq("username", user.username)) > 0) {
            log.warn("用户名已存在")
            throw CustomException("用户名已存在")
        }
        MongoUtil.collection.insertOne(Document.parse(JSON.toJSONString(user)))
        return 1
//        //检查用户名重复
//        //处理用户角色
//        if (vo.roleIds != null) {
//            updateRole(user.id!!, vo.roleIds!!)
//        }
    }

    fun updatePassword(vo: UserVo) {
        if (MongoUtil.collection.countDocuments(Filters.eq("username", vo.username)) == 0L) {
            log.warn("用户名不存在")
            throw CustomException("用户名不存在")
        }
        MongoUtil.collection.updateOne(
            eq("username", vo.username),
            combine(
                set("password", BCryptPasswordEncoder().encode(vo.password)),
                set("updateTime", System.currentTimeMillis())
            )
        )
    }
}
