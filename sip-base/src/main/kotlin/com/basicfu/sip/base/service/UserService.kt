package com.basicfu.sip.base.service

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.basicfu.sip.base.common.constant.Constant
import com.basicfu.sip.base.common.enum.Enum
import com.basicfu.sip.base.mapper.AppMapper
import com.basicfu.sip.base.mapper.MenuMapper
import com.basicfu.sip.base.model.biz.RoleToken
import com.basicfu.sip.base.model.dto.MenuDto
import com.basicfu.sip.base.model.dto.UserDto
import com.basicfu.sip.base.model.po.Menu
import com.basicfu.sip.base.model.po.User
import com.basicfu.sip.base.model.vo.UserVo
import com.basicfu.sip.base.util.*
import com.basicfu.sip.base.util.MongoUtil.collection
import com.basicfu.sip.core.common.exception.CustomException
import com.basicfu.sip.core.common.mapper.example
import com.basicfu.sip.core.common.mapper.generate
import com.basicfu.sip.core.model.vo.BaseVo
import com.basicfu.sip.core.util.RedisUtil
import com.basicfu.sip.core.util.log
import com.github.pagehelper.PageInfo
import com.mongodb.client.FindIterable
import com.mongodb.client.model.Filters.*
import com.mongodb.client.model.Sorts
import com.mongodb.client.model.Updates.combine
import com.mongodb.client.model.Updates.set
import org.bson.Document
import org.bson.conversions.Bson
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

/**
 * @author basicfu
 * @date 2018/6/30
 */
@Service
class UserService {
    @Autowired
    lateinit var roleService: RoleService
    @Autowired
    lateinit var menuMapper: MenuMapper
    @Autowired
    lateinit var appMapper: AppMapper

    fun status(): UserDto {
        var user = TokenUtil.getCurrentUser()
        if (user == null) {
            user = generate<UserDto> {
                roles = listOf(Constant.System.GUEST)
            }
        }
        return dealUser(user)
    }

    fun get(vo: UserVo): UserDto? {
        if (vo.id == null && vo.username == null) {
            throw CustomException("非法参数")
        }
        val userDocument = if (vo.id != null) {
            collection.find(and(eq("_id", ObjectId(vo.id)), eq("isdel", false))).first() ?: return null
        } else {
            collection.find(and(eq("username", vo.username), eq("isdel", false))).first() ?: return null
        }
        val json = JSON.parseObject(JSON.toJSONString(userDocument))
        val uid = userDocument.getObjectId("_id").toHexString()
        json["id"] = uid
        json.remove("_id")
        json.remove("password")
        json["roles"] = roleService.listRoleByUid(uid)
        return dealUser(JSON.toJavaObject(json, UserDto::class.java))
    }

    /**
     * 分页暂时采用skip+limit
     * 变慢以后采用find+limit
     */
    fun list(vo: UserVo): PageInfo<UserDto> {
        val page = BaseVo().setInfo()
        val filters = arrayListOf<Bson>()
        val documents: FindIterable<Document>
        val total: Int
        if (vo.q != null) {
            filters.add(regex("username", "${vo.q}"))
            filters.add(regex("nickname", "${vo.q}"))
            filters.add(regex("mobile", "${vo.q}"))
            filters.add(regex("email", "${vo.q}"))
            documents = collection.find(or(filters)).sort(Sorts.descending("createTime"))
                .skip((page.pageNum - 1) * page.pageSize).limit(page.pageSize)
            total = documents.count()
        } else {
            documents = collection.find().sort(Sorts.descending("createTime")).skip((page.pageNum - 1) * page.pageSize)
                .limit(page.pageSize)
            total = documents.count()
        }
        val list = arrayListOf<UserDto>()
        documents.forEach {
            val json = JSON.parseObject(JSON.toJSONString(it))
            json["id"] = it.getObjectId("_id").toHexString()
            list.add(json.toJavaObject(UserDto::class.java))
        }
        val pageInfo = PageInfo<UserDto>(list)
        pageInfo.pageNum = page.pageNum
        pageInfo.pageSize = page.pageSize
        pageInfo.total = total.toLong()
        return pageInfo
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
    fun login(vo: UserVo): JSONObject {
        val username = vo.username!!
        val collection = MongoUtil.collection
        var userDocument = collection.find(and(eq("username", username), eq("isdel", false))).first()
        if (userDocument == null) {
            //尝试手机登录、需要已验证的手机号
            userDocument =
                collection.find(and(eq("mobile", username), eq("mobileVerified", true), eq("isdel", false))).first()
        }
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
        val json = JSON.parseObject(JSON.toJSONString(userDocument))
        val uid = userDocument.getObjectId("_id").toHexString()
        json["id"] = uid
        json["token"] = TokenUtil.generateFrontToken(token) ?: throw CustomException(Enum.LOGIN_ERROR)
        json.remove("_id")
        json.remove("password")
        json["roles"] = roleService.listRoleByUid(uid)
        RedisUtil.set(TokenUtil.generateRedisToken(token), json.toJSONString(), Constant.System.SESSION_TIMEOUT)
        return json
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
     * 注册方式、检查系统启用哪些注册方式
     * 用户名密码 0
     * 用户名密码+手机号验证 1
     * 用户名密码+邮箱验证 2
     * 后台创建 3
     */
    fun insert(map: Map<String, Any>): Int {
        val type = map["type"].toString().toInt()
        val user = generate<User> {
            username = map["username"].toString()
            nickname = map["nickname"]?.toString()
            email = map["email"]?.toString()
            mobile = map["mobile"]?.toString()
            password = BCryptPasswordEncoder().encode(map["password"].toString())
            mobileVerified = false
            emailVerified = false
            createTime = System.currentTimeMillis()
            updateTime = createTime
            blocked = false
            isdel = false
            registerType = type
        }
        if (MongoUtil.collection.countDocuments(and(eq("username", user.username), eq("isdel", false))) > 0) {
            log.warn("用户名已存在")
            throw CustomException("用户名已存在")
        }
        when (type) {
            Enum.RegisterType.USERNAME.value -> {

            }
            Enum.RegisterType.USERNAME_MOBILE.value -> {
                val code = map["code"]?.toString()
                if (user.mobile == null) throw CustomException("手机号不能为空")
                if (code == null) throw CustomException("验证码不能为空")
                val smsKey = "${Constant.Redis.SMS_CHECK}${user.mobile}"
                val redisCode = RedisUtil.get<String>(smsKey)
                if (redisCode == null || redisCode != code) throw CustomException("验证码错误")
                user.mobileVerified = true
                RedisUtil.del(smsKey)
            }
            Enum.RegisterType.USERNAME_EMAIL.value -> {

            }
            Enum.RegisterType.SYSTEM.value -> {

            }
            else -> {
                throw CustomException("不支持的注册方式")
            }
        }
        MongoUtil.collection.insertOne(Document.parse(JSON.toJSONString(user)))
        return 1
//        //检查用户名重复
//        //处理用户角色
//        if (vo.roleIds != null) {
//            updateRole(user.id!!, vo.roleIds!!)
//        }
    }
    fun update(map: Map<String, Any>):Int{
        return 0
    }
    fun checkMobile(mobile: String) {
        if (MongoUtil.collection.countDocuments(and(eq("mobile", mobile), eq("isdel", false))) > 0) {
            log.warn("手机号已存在")
            throw CustomException("手机号已存在")
        }
    }

    fun updatePassword(vo: UserVo) {
        val user = TokenUtil.getCurrentUser()!!
        val password=MongoUtil.collection.find(and(eq("username", user.username), eq("isdel", false))).first().getString("password")
        if(password!=BCryptPasswordEncoder().encode(vo.orignPassword)){
            throw CustomException("原密码不正确")
        }
        MongoUtil.collection.updateOne(
            eq("username", vo.username),
            combine(
                set("password", BCryptPasswordEncoder().encode(vo.password)),
                set("updateTime", System.currentTimeMillis())
            )
        )
    }

    fun findPassword(vo: UserVo) {
        val code=vo.code?:throw CustomException("验证码不能为空")
        val smsKey = "${Constant.Redis.SMS_CHECK}${vo.mobile}"
        val redisCode = RedisUtil.get<String>(smsKey)
        if (redisCode == null || redisCode != code) throw CustomException("验证码错误")
        RedisUtil.del(smsKey)

        val user = TokenUtil.getCurrentUser()!!
        val password=MongoUtil.collection.find(and(eq("username", user.username), eq("isdel", false))).first().getString("password")
        if(password!=BCryptPasswordEncoder().encode(vo.orignPassword)){
            throw CustomException("原密码不正确")
        }
        MongoUtil.collection.updateOne(
            eq("username", vo.username),
            combine(
                set("password", BCryptPasswordEncoder().encode(vo.password)),
                set("updateTime", System.currentTimeMillis())
            )
        )
    }

    /***
     * 删除用户ids,type0逻辑删除,1物理删除
     * 删除用户时清空当前用户的在线信息
     * 配置用户删除时的回调
     */
    fun delete(ids: List<String>, type: Int) {
        if (type == 0) {
            MongoUtil.collection.updateMany(and(ids.map { eq("_id", ObjectId(it)) }), set("isdel", true))
        } else if (type == 1) {
            MongoUtil.collection.deleteMany(and(ids.map { eq("_id", ObjectId(it)) }))
        }
        val usernames =
            MongoUtil.collection.find(and(ids.map { eq("_id", ObjectId(it)) })).map { it.getString("username") }
        usernames.forEach { username ->
            val keys = RedisUtil.keys(TokenUtil.getRedisUserTokenPrefix(username) + "*")
            RedisUtil.del(keys.map { it })
        }
    }

    /**
     * 处理用户角色菜单信息
     */
    private fun dealUser(user: UserDto): UserDto {
        val menuIds = arrayListOf<Long>()
        val appMap = appMapper.selectAll().associateBy({ it!!.id!! }, { it!!.code!! })
        if (user.id == null) {
            menuIds.addAll(
                RedisUtil.hGet<RoleToken>(Constant.Redis.ROLE_PERMISSION, Constant.System.GUEST)?.menus
                    ?: emptyList()
            )
        } else {
            val roles = user.roles.plus(Constant.System.GUEST)
            val appRoleToken = RedisUtil.hGetAll<RoleToken>(Constant.Redis.ROLE_PERMISSION)
            menuIds.addAll(appRoleToken.filter { roles.contains(it.key) }.values.map { it!!.menus }.flatten())
        }
        if (menuIds.isNotEmpty()) {
            val allMenu = copy<MenuDto>(menuMapper.selectByExample(example<Menu> {
                andEqualTo {
                    display = true
                }
                andIn(Menu::id, menuIds)
            }).toList())
            val menus = MenuUtil.recursive(null, allMenu).groupBy({ appMap[it.appId]!! }, { it })
            user.menus = menus
        }
        user.token = TokenUtil.getCurrentFrontToken()
        return user
    }
}
