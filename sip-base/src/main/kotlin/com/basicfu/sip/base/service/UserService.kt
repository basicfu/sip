package com.basicfu.sip.base.service

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.basicfu.sip.base.common.constant.Constant
import com.basicfu.sip.base.common.enum.Enum
import com.basicfu.sip.base.mapper.AppMapper
import com.basicfu.sip.base.mapper.MenuMapper
import com.basicfu.sip.base.model.biz.RoleToken
import com.basicfu.sip.base.model.dto.MenuDto
import com.basicfu.sip.base.model.po.Menu
import com.basicfu.sip.base.model.po.User
import com.basicfu.sip.base.model.vo.UserVo
import com.basicfu.sip.base.util.MenuUtil
import com.basicfu.sip.base.util.PasswordUtil
import com.basicfu.sip.base.util.TokenUtil
import com.basicfu.sip.base.util.copy
import com.basicfu.sip.core.common.exception.CustomException
import com.basicfu.sip.core.common.mapper.example
import com.basicfu.sip.core.common.mapper.generate
import com.basicfu.sip.core.model.vo.BaseVo
import com.basicfu.sip.core.util.HttpUtil
import com.basicfu.sip.core.util.RedisUtil
import com.basicfu.sip.core.util.log
import com.github.pagehelper.PageInfo
import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.arrayListOf
import kotlin.collections.associateBy
import kotlin.collections.emptyList
import kotlin.collections.filter
import kotlin.collections.flatten
import kotlin.collections.forEach
import kotlin.collections.get
import kotlin.collections.groupBy
import kotlin.collections.isNotEmpty
import kotlin.collections.listOf
import kotlin.collections.map
import kotlin.collections.plus
import kotlin.collections.set
import kotlin.collections.toList


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
    @Autowired
    lateinit var mongoTemplate: MongoTemplate

    /**
     * 提供两种模式
     * 简版-redis缓存的信息(一般用于后台调用)
     * 完整版-包含菜单信息(一般用于前台页面显示)
     */
    fun status(type:String?): JSONObject {
        var user = TokenUtil.getCurrentUserJson()
        if (user == null) {
            user=JSONObject()
            user["roles"]=listOf(Constant.System.GUEST)
        }
        return if(type!=null&&type=="full"){
            dealUser(user)
        }else{
            user
        }
    }

    /**
     * 提供两种模式
     * 简版-redis缓存的信息(一般用于后台调用)
     * 完整版-包含菜单信息(一般用于前台页面显示)
     */
    fun get(vo: UserVo): JSONObject? {
        if (vo.id == null && vo.username == null) {
            throw CustomException("非法参数")
        }
        val userDocument = (if (vo.id != null) {
            mongoTemplate.findOne(
                Query(Criteria.where("isdel").`is`(false).and("_id").`is`(vo.id)),
                Document::class.java,
                "user"
            )
        } else {
            mongoTemplate.findOne(
                Query(Criteria.where("isdel").`is`(false).and("username").`is`(vo.username)),
                Document::class.java,
                "user"
            )
        })
        if (userDocument != null) {
            val user = sec(userDocument)
            user["roles"] = roleService.listRoleByUid(user.getString("id"))
            return if(vo.type!=null&&vo.type=="full"){
                dealUser(user)
            }else{
                user
            }
        }
        return null
    }

    /**
     * 分页暂时采用skip+limit
     * 变慢以后采用find+limit
     */
    fun list(vo: UserVo): PageInfo<JSONObject> {
        val page = BaseVo().setInfo()
        val documents: List<Document>
        val query = Query()
        if (vo.q != null) {
            query.addCriteria(
                Criteria().orOperator(
                    Criteria.where("username").regex("${vo.q}"),
                    Criteria.where("nickname").regex("${vo.q}"),
                    Criteria.where("mobile").regex("${vo.q}"),
                    Criteria.where("email").regex("${vo.q}")
                )
            )
        }
        query.with(Sort(Sort.Direction.DESC, "createTime"))
            .skip((page.pageNum - 1) * page.pageSize.toLong()).limit(page.pageSize)
        documents = mongoTemplate.find(query, Document::class.java, "user")
        val total = mongoTemplate.count(query, Document::class.java, "user")
        val list = arrayListOf<JSONObject>()
        documents.forEach {
            list.add(sec(it))
        }
        val pageInfo = PageInfo<JSONObject>(list)
        pageInfo.pageNum = page.pageNum
        pageInfo.pageSize = page.pageSize
        pageInfo.total = total
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
        var userDocument = mongoTemplate.findOne(
            Query(Criteria.where("isdel").`is`(false).and("username").`is`(username)),
            Document::class.java,
            "user"
        )
        if (userDocument == null) {
            //尝试手机登录、需要已验证的手机号
            userDocument = mongoTemplate.findOne(
                Query(Criteria.where("isdel").`is`(false).and("mobile").`is`(username).and("mobileVerified").`is`(true)),
                Document::class.java,
                "user"
            )
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
        val json = sec(userDocument)
        json["token"] = TokenUtil.generateFrontToken(token) ?: throw CustomException(Enum.LOGIN_ERROR)
        json["roles"] = roleService.listRoleByUid(json.getString("id"))
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
     * TODO注册权限问题
     */
    fun insert(map: Map<String, String>): Int {
        val type = map["type"].toString().toInt()
        val user = generate<User> {
            username = map["username"].toString()
            nickname = map["nickname"]
            email = map["email"]
            mobile = map["mobile"]
            password = BCryptPasswordEncoder().encode(map["password"].toString())
            mobileVerified = false
            emailVerified = false
            createTime = System.currentTimeMillis()
            updateTime = createTime
            blocked = false
            isdel = false
            registerType = type
        }
        if (mongoTemplate.count(
                Query(Criteria.where("isdel").`is`(false).and("username").`is`(user.username)),
                "user"
            ) > 0
        ) {
            log.warn("用户名已存在")
            throw CustomException("用户名已存在")
        }
        when (type) {
            Enum.RegisterType.USERNAME.value -> {
                //TODO
            }
            Enum.RegisterType.USERNAME_MOBILE.value -> {
                val code = map["code"]
                if (user.mobile == null) throw CustomException("手机号不能为空")
                if (code == null) throw CustomException("验证码不能为空")
                val smsKey = "${Constant.Redis.SMS_CHECK}${user.mobile}"
                val redisCode = RedisUtil.get<String>(smsKey)
                if (redisCode == null || redisCode != code) throw CustomException("验证码错误")
                user.mobileVerified = true
                RedisUtil.del(smsKey)
            }
            Enum.RegisterType.USERNAME_EMAIL.value -> {
                //TODO
            }
            Enum.RegisterType.SYSTEM.value -> {
                //TODO
            }
            else -> {
                throw CustomException("不支持的注册方式")
            }
        }
        val u=mongoTemplate.insert(user)
        val resultMap=map.toMutableMap()
        resultMap["id"]=u.id!!
        //处理每个应用的注册回调
        appMapper.selectAll().filter { !it.callback.isNullOrBlank() }.forEach {
            try {
                HttpUtil.postJson(it.callback!!,null,resultMap)
                log.info("注册回调完成")
            }catch (e:Exception){
                log.error("回调失败")
            }
        }
        return 1
//        //处理用户角色
//        if (vo.roleIds != null) {
//            updateRole(user.id!!, vo.roleIds!!)
//        }
    }

    fun update(map: Map<String, Any>): Int {
        //参数强校验
        val uid= map["id"]
        val username=map["username"]
        val mobile=map["username"]
        val email=map["username"]
        val update=Update()
        map.forEach { k, v ->
            //此处严谨应排除所有系统bean字段
            if(k!="id"){
                update.addToSet(k,v)
            }
        }
        //校验用户名、手机、邮箱重复
        if(mongoTemplate.count(Query(Criteria.where("username").`is`(username).and("isdel").`is`(false).and("id").ne(uid)),User::class.java)>0){
            throw CustomException("用户名已存在")
        }
        if(mobile!=null&&mongoTemplate.count(Query(Criteria.where("mobile").`is`(mobile).and("isdel").`is`(false).and("id").ne(uid)),User::class.java)>0){
            throw CustomException("手机号已存在")
        }
        if(email!=null&&mongoTemplate.count(Query(Criteria.where("email").`is`(email).and("isdel").`is`(false).and("id").ne(uid)),User::class.java)>0){
            throw CustomException("邮箱已存在")
        }
        mongoTemplate.updateFirst(
            Query(Criteria.where("_id").`is`(uid)),update,"user"
        )
        return 1
    }

    fun checkMobile(mobile: String) {
        if (mongoTemplate.count(
                Query(Criteria.where("isdel").`is`(false).and("mobile").`is`(mobile)),
                "user"
            ) > 0
        ) {
            log.warn("手机号已存在")
            throw CustomException("手机号已存在")
        }
    }

    fun updatePassword(vo: UserVo) {
        val user = TokenUtil.getCurrentUser()!!
        val password = mongoTemplate.findOne(
            Query(Criteria.where("username").`is`(user.username).and("isdel").`is`(false)),
            User::class.java
        )!!.password
        if (password != BCryptPasswordEncoder().encode(vo.orignPassword)) {
            throw CustomException("原密码不正确")
        }
        mongoTemplate.updateFirst(
            Query(Criteria.where("username").`is`(user.username).and("isdel").`is`(false)),
            Update.update("password", BCryptPasswordEncoder().encode(vo.password)).addToSet(
                "updateTime",
                System.currentTimeMillis()
            ),
            "user"
        )
    }

    fun findPassword(vo: UserVo) {
        val code = vo.code ?: throw CustomException("验证码不能为空")
        val smsKey = "${Constant.Redis.SMS_CHECK}${vo.mobile}"
        val redisCode = RedisUtil.get<String>(smsKey)
        if (redisCode == null || redisCode != code) throw CustomException("验证码错误")
        RedisUtil.del(smsKey)
        updatePassword(vo)
    }

    /***
     * 删除用户ids,type0逻辑删除,1物理删除
     * 删除用户时清空当前用户的在线信息
     * 配置用户删除时的回调
     */
    fun delete(ids: List<String>, type: Int) {
        if (ids.isEmpty()) {
            return
        }
        val criteria = Criteria()
        ids.map {
            criteria.and("_id").`is`(it)
        }
        mongoTemplate.find(Query(criteria), User::class.java).map { it.username!! }.forEach { username ->
            val keys = RedisUtil.keys(TokenUtil.getRedisUserTokenPrefix(username) + "*")
            RedisUtil.del(keys.map { it })
        }
        if (type == 0) {
            mongoTemplate.updateMulti(Query(criteria), Update.update("isdel", true), "user")
        } else if (type == 1) {
            mongoTemplate.remove(Query(criteria), "user")
        }
    }

    private fun sec(document: Document): JSONObject {
        val json = JSON.parseObject(JSON.toJSONString(document))
        val uid = document.getObjectId("_id").toHexString()
        json["id"] = uid
        json.remove("_id")
        json.remove("password")
        return json
    }

    /**
     * 处理用户角色菜单信息
     */
    private fun dealUser(user: JSONObject): JSONObject {
        val menuIds = arrayListOf<Long>()
        val appMap = appMapper.selectAll().associateBy({ it!!.id!! }, { it!!.code!! })
        val id=user.getString("id")
        val roles=user.getJSONArray("roles")
        if (id == null) {
            menuIds.addAll(
                RedisUtil.hGet<RoleToken>(Constant.Redis.ROLE_PERMISSION, Constant.System.GUEST)?.menus
                    ?: emptyList()
            )
        } else {
            val appRoleToken = RedisUtil.hGetAll<RoleToken>(Constant.Redis.ROLE_PERMISSION)
            menuIds.addAll(appRoleToken.filter { roles.plus(Constant.System.GUEST).contains(it.key) }.values.map { it!!.menus }.flatten())
        }
        if (menuIds.isNotEmpty()) {
            val allMenu = copy<MenuDto>(menuMapper.selectByExample(example<Menu> {
                andEqualTo {
                    display = true
                }
                andIn(Menu::id, menuIds)
            }).toList())
            val menus = MenuUtil.recursive(null, allMenu).groupBy({ appMap[it.appId]!! }, { it })
            user["menus"] = menus
        }
        user["token"] = TokenUtil.getCurrentFrontToken()
        return user
    }
}
