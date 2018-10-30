package com.basicfu.sip.base.service

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.basicfu.sip.base.common.Enum
import com.basicfu.sip.base.common.Enum.Condition
import com.basicfu.sip.base.common.Enum.FieldType.*
import com.basicfu.sip.base.mapper.UserAuthMapper
import com.basicfu.sip.base.mapper.UserMapper
import com.basicfu.sip.base.model.po.User
import com.basicfu.sip.base.model.po.UserAuth
import com.basicfu.sip.base.model.vo.UserVo
import com.basicfu.sip.base.util.PasswordUtil
import com.basicfu.sip.client.util.DictUtil
import com.basicfu.sip.core.common.Constant
import com.basicfu.sip.core.common.Enum.UserType
import com.basicfu.sip.core.common.exception.CustomException
import com.basicfu.sip.core.common.mapper.example
import com.basicfu.sip.core.common.mapper.generate
import com.basicfu.sip.core.model.dto.AppDto
import com.basicfu.sip.core.model.dto.ResourceDto
import com.basicfu.sip.core.model.dto.UserDto
import com.basicfu.sip.core.model.vo.BaseVo
import com.basicfu.sip.core.service.BaseService
import com.basicfu.sip.core.util.*
import com.github.pagehelper.PageHelper
import com.github.pagehelper.PageInfo
import org.apache.commons.lang3.StringUtils
import org.apache.ibatis.session.RowBounds
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.text.SimpleDateFormat

/**
 * @author basicfu
 * @date 2018/6/30
 */
@Service
class UserService : BaseService<UserMapper, User>() {
    @Autowired
    lateinit var userAuthMapper: UserAuthMapper
    @Autowired
    lateinit var userTemplateService: UserTemplateService

    fun getCurrentUser(): JSONObject? {
        var user = TokenUtil.getCurrentUser()
        if (user == null) {
            user = TokenUtil.getGuestUser()
        }
        return UserUtil.toJson(user)
    }

    fun get(id: Long): JSONObject? {
        val result = to<UserDto>(mapper.selectByPrimaryKey(id))
        if (result != null) {
            val list = arrayListOf(result)
            dealReturnUser(list)
            return UserUtil.toJson(list[0])
        }
        return null
    }

    /**
     * 支持多个and条件，暂不支持or
     * 更多查询条件待开发
     */
    fun list(vo: UserVo): PageInfo<JSONObject> {
        val pageList: PageInfo<UserDto>
        val userTemplateMap = userTemplateService.all().associateBy { it.enName }
        val conditionJson = vo.condition?.let { JSON.parseObject(it) } ?: JSONObject()
        val roleCode = conditionJson.getString("roleCode")
        if (roleCode == null) {
            pageList = selectPage(example<User> {
                exists(false)
                conditionJson.forEach { k, v ->
                    //是系统字段不应该使用json in
                    val systemField = !userTemplateMap.containsKey(k)
                    val key = if (!systemField) {
                        "content->'$.$k'"
                    } else {
                        k
                    }
                    //判断是否是系统字段
                    val parameterObj = JSON.parseObject(v.toString())
                    val condition = Enum.Condition.valueOf(parameterObj.getString("condition"))
                    val value = parameterObj["value"]
                    when (condition) {
                        Condition.IS_NULL -> andCondition(key, "is null")
                        Condition.IS_NOT_NULL -> andCondition(key, "is not null")
                        Condition.EQUAL_TO -> andCondition(key, "= '$value'")
                        Condition.NOT_EQUAL_TO -> andCondition(key, "<> '$value'")
                        Condition.GREATER_THEN -> andCondition(key, "> '$value'")
                        Condition.GREATER_THEN_OR_EQUAL_TO -> andCondition(key, ">= '$value'")
                        Condition.LESS_THEN -> andCondition(key, "< '$value'")
                        Condition.LESS_THEN_OR_EQUAL_TO -> andCondition(key, "<= '$value'")
                        Condition.IN -> {
                            //目前系统字段没有需要in的
                            if (!systemField) {
                                andCondition("", "JSON_CONTAINS(user.content,'$value','\$.$k')=1")
                            }
                        }
                        Condition.NOT_IN -> {
                            if (!systemField) {
                                andCondition("", "JSON_CONTAINS(user.content,'$value','\$.$k')=0")
                            }
                        }
                        Condition.BETWEEN -> {
                            val values = JSON.parseArray(value.toString())
                            andCondition(key, "between ${values[0]} and ${values[1]}")
                        }
                        Condition.NOT_BETWEEN -> {
                            val values = JSON.parseArray(value.toString())
                            andCondition(key, "not between ${values[0]} and ${values[1]}")
                        }
                        Condition.LIKE -> andCondition(key, "like ${SqlUtil.dealLikeValue(value.toString())}")
                        Condition.NOT_LIKE -> andCondition(
                            key,
                            "not like ${SqlUtil.dealLikeValue(value.toString())}"
                        )
                    }
                }
                orderByDesc(User::cdate)
            })
        } else {
            val page = BaseVo().setInfo()
            PageHelper.startPage<Any>(page.pageNum, page.pageSize)
            val orignSql = "SELECT DISTINCT u.* FROM `sip-base`.user u " +
                    "LEFT JOIN `sip-permission`.user_role ur on u.id=ur.user_id " +
                    "LEFT JOIN `sip-permission`.role r on ur.role_id=r.id " +
                    "WHERE "
            var sql=""
            //必有role
            conditionJson.forEach { k, v ->
                val systemField = !userTemplateMap.containsKey(k)
                val key = if (!systemField) {
                    "u.content->'$.$k'"
                } else {
                    if (k == "roleCode") {
                        "r.code"
                    } else {
                        "u.$k"
                    }
                }
                //判断是否是系统字段
                val parameterObj = JSON.parseObject(v.toString())
                val condition = Enum.Condition.valueOf(parameterObj.getString("condition"))
                val value = parameterObj["value"]
                when (condition) {
                    Condition.IS_NULL -> sql += "and $key is null"
                    Condition.IS_NOT_NULL -> sql += "and $key is not null"
                    Condition.EQUAL_TO -> sql += "and $key = '$value'"
                    Condition.NOT_EQUAL_TO -> sql += "and $key <> '$value'"
                    Condition.GREATER_THEN -> sql += "and $key > '$value'"
                    Condition.GREATER_THEN_OR_EQUAL_TO -> sql += "and $key >= '$value'"
                    Condition.LESS_THEN -> sql += "and $key < '$value'"
                    Condition.LESS_THEN_OR_EQUAL_TO -> sql += "and $key <= '$value'"
                    Condition.IN -> {
                        sql += if (!systemField) {
                            " and JSON_CONTAINS(u.content,'$value','\$.$k')=1"
                        } else {
                            " and $key in ('${StringUtils.join(JSON.parseArray(value.toString()).map { it }, "','")}')"
                        }
                    }
                    Condition.NOT_IN -> {
                        sql += if (!systemField) {
                            " and JSON_CONTAINS(u.content,'$value','\$.$k')=0"
                        } else {
                            " and $key not in ('${StringUtils.join(JSON.parseArray(value.toString()).map { it }, "','")}')"
                        }
                    }
                    Condition.BETWEEN -> {
                        val values = JSON.parseArray(value.toString())
                        sql += " between ${values[0]} and ${values[1]}"
                    }
                    Condition.NOT_BETWEEN -> {
                        val values = JSON.parseArray(value.toString())
                        sql += "not between ${values[0]} and ${values[1]}"
                    }
                    Condition.LIKE -> sql += "and $key like ${SqlUtil.dealLikeValue(value.toString())}"
                    Condition.NOT_LIKE -> sql += "and $key not like ${SqlUtil.dealLikeValue(value.toString())}"
                }
            }
            sql = sql.trimStart()
            if(sql.startsWith("and")){
                sql=sql.substringAfter("and")
            }
            val result = mapper.selectBySql(orignSql+sql)
            pageList = PageInfo(to<UserDto>(result))
        }
        val users = pageList.list
        dealReturnUser(users)
        val result = PageInfo<JSONObject>()
        BeanUtils.copyProperties(pageList, result)
        result.list = UserUtil.toJson(users)
        return result
    }

    fun listByIds(ids: List<Long>): List<JSONObject>? {
        val users = to<UserDto>(mapper.selectByExample(example<User> {
            andIn(User::id, ids)
        }))
        dealReturnUser(users)
        return UserUtil.toJson(users)
    }

    fun listUsernameByIds(ids: List<Long>): List<JSONObject>? {
        val users = to<UserDto>(mapper.selectByExample(example<User> {
            select(User::id, User::username)
            andIn(User::id, ids)
        }))
        return UserUtil.toJson(users)
    }

    /**
     * type
     * ROLE:按照角色code查询,如果使用按角色查询需保证permission和base在一个数据库地址中
     * ALL:(default)包含以下三种登录方式
     * USERNAME:只按照用户名
     * MOBILE:只按照手机号
     * EMAIL:只按照邮箱
     */
    fun suggest(q: String, roleCode: String?, size: Int): List<JSONObject>? {
        //可测试性能
//        mapper.selectBySql("SELECT u.id,u.tenant_id,u.content,u.cdate,u.udate,status,ua.username,ua.type from user u LEFT JOIN user_auth ua on u.id=ua.uid INNER JOIN (select DISTINCT(uid) from user_auth WHERE username like '%1%' limit 10) as sub on u.id=sub.uid;")
        val userIds: List<Long>
        if (roleCode.isNullOrBlank()) {
            userIds = userAuthMapper.selectByExampleAndRowBounds(example<UserAuth> {
                select(UserAuth::uid)
                distinct()
                andLike {
                    username = q
                }
            }, RowBounds(0, size)).mapNotNull { it.uid }
        } else {
            val users = mapper.selectBySql(
                "SELECT DISTINCT uid as id FROM `sip-base`.user_auth ua " +
                        "RIGHT JOIN `sip-permission`.user_role ur on ua.uid=ur.user_id " +
                        "LEFT JOIN `sip-permission`.role r on ur.role_id=r.id " +
                        "WHERE ua.username LIKE ${SqlUtil.dealLikeValue(q)} AND r.code='$roleCode' LIMIT 0,$size"
            )
            userIds = users.map { it.id!! }
        }
        val users = to<UserDto>(selectByIds(userIds))
        dealReturnUser(users)
        return UserUtil.toJson(users)
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
     * TODO 登录查询表过多，可优化连为连表查询
     *
     */
    fun login(vo: UserVo): JSONObject? {
        val userAuth: UserAuth
        var username = vo.username!!
        //如果登录的应用是sip则不按照应用过滤查询用户,如果登录的是其他应用会切换为其他应用
        var appId = AppUtil.getAppId()
        var appCode = AppUtil.getAppCode()
        if (appCode == Constant.System.APP_SYSTEM_CODE) {
            if (username.contains("@")) {
                //如果用户名有@则是应用普通管理员,只获取用户类型为APP_ADMIN
                val app =
                    RedisUtil.hGet<AppDto>(Constant.Redis.APP, username.substringBefore("@")) ?: throw CustomException(
                        Enum.User.USERNAME_OR_PASSWORD_ERROR
                    )
                appId = app.id
                appCode = app.code
                username = username.substringAfter("@")
                AppUtil.appNotCheck()
                val userAuths = userAuthMapper.select(generate {
                    this.appId = appId
                    type = com.basicfu.sip.core.common.Enum.LoginType.USERNAME.value
                    this.username = username
                })
                if (userAuths.isEmpty()) {
                    throw CustomException(Enum.User.USERNAME_OR_PASSWORD_ERROR)
                }
                val userAuthMap = userAuths.associateBy { it.uid }
                AppUtil.appNotCheck()
                val selectByIds = selectByIds(userAuths.map { it.uid!! })
                val filter = selectByIds.filter { it.type == com.basicfu.sip.core.common.Enum.UserType.APP_ADMIN.name }
                if (filter.isEmpty()) {
                    throw CustomException(Enum.User.USERNAME_OR_PASSWORD_ERROR)
                }
                userAuth = userAuthMap[filter[0].id]!!
            } else {
                //三种用户类型，可能属于多个应用，但用户名唯一
                AppUtil.appNotCheck()
                val userAuths = userAuthMapper.select(generate {
                    this.username = username
                    type = com.basicfu.sip.core.common.Enum.LoginType.USERNAME.value
                })
                if (userAuths.isEmpty()) {
                    throw CustomException(Enum.User.USERNAME_OR_PASSWORD_ERROR)
                }
                val userAuthMap = userAuths.associateBy { it.uid }
                AppUtil.appNotCheck()
                val selectByIds = selectByIds(userAuths.map { it.uid!! })
                val filter = selectByIds.filter {
                    listOf(
                        com.basicfu.sip.core.common.Enum.UserType.SYSTEM_SUPER_ADMIN.name,
                        com.basicfu.sip.core.common.Enum.UserType.SYSTEM_ADMIN.name,
                        com.basicfu.sip.core.common.Enum.UserType.APP_SUPER_ADMIN.name
                    ).contains(it.type)
                }
                if (filter.isEmpty()) {
                    throw CustomException(Enum.User.USERNAME_OR_PASSWORD_ERROR)
                }
                userAuth = userAuthMap[filter[0].id]!!
            }
        } else {
            userAuth = userAuthMapper.selectOne(generate {
                this.username = username
                type = com.basicfu.sip.core.common.Enum.LoginType.USERNAME.value
            }) ?: throw CustomException(Enum.User.USERNAME_OR_PASSWORD_ERROR)
        }
        if (!PasswordUtil.matches(username + vo.password!!, userAuth.password!!)) throw CustomException(
            Enum.User.USERNAME_OR_PASSWORD_ERROR
        )
        //TODO 界面配置登录模式
        val loginModal = 3
        @Suppress("ConstantConditionIf")
        if (loginModal == 1) {
            val keys = RedisUtil.keys(TokenUtil.getRedisUserTokenPrefix(username) + "*")
            if (keys.size >= 2) {
                throw CustomException(Enum.User.THEN_USER_MAX_ONLINE)
            }
        } else if (loginModal == 2) {
            val keys = RedisUtil.keys(TokenUtil.getRedisUserTokenPrefix(username) + "*")
            RedisUtil.del(keys.map { it })
        }
        AppUtil.appNotCheck()
        val user = to<UserDto>(mapper.selectByPrimaryKey(userAuth.uid))
        val currentTime = (System.currentTimeMillis() / 1000).toInt()
        userAuthMapper.updateByPrimaryKeySelective(generate {
            id = userAuth.id
            ldate = currentTime
        })
        // 后续需要更改当前应用ID
        val appInfo = JSONObject()
        appInfo[Constant.System.APP_ID] = appId
        appInfo[Constant.System.APP_CODE] = appCode
        AppUtil.updateApp(appInfo)
        val permission =
            com.basicfu.sip.client.util.UserUtil.getPermissionByUidJson(user!!.id!!) ?: throw CustomException(
                Enum.User.LOGIN_ERROR
            )
        val userAuths = userAuthMapper.select(generate {
            uid = userAuth.uid
        }).associateBy({ it.type }, { it })
        user.appCode = appCode
        user.mobile = userAuths[1]?.username
        user.email = userAuths[2]?.username
        user.ldate = currentTime
        user.roles = permission.getJSONArray("roles")
        user.menus = permission.getJSONArray("menus")
        user.permissions = permission.getJSONArray("permissions")
        user.resources = permission.getJSONArray("resources").toJavaList(ResourceDto::class.java)
            .groupBy({ it.serviceId.toString() }, { "/" + it.method + it.url })
        //TODO 系统设置登录过期时间
        val userToken = TokenUtil.generateUserToken(username)
        val redisToken = TokenUtil.getRedisToken(userToken)
        RedisUtil.set(redisToken, user, Constant.System.SESSION_TIMEOUT)
        val frontToken = TokenUtil.generateFrontToken(userToken) ?: throw CustomException(Enum.User.LOGIN_ERROR)
        val result = JSONObject()
        result["token"] = frontToken
        result["time"] = System.currentTimeMillis() / 1000
        result["username"] = user.username
        result["roles"] = user.roles
        return result
    }

    /**
     * 登出
     * TODO 如果是模式2需要移除所有token
     */
    fun logout() {
        TokenUtil.getCurrentToken()?.let {
            RedisUtil.del(Constant.Redis.TOKEN_PREFIX + it)
        }
    }

    /**
     * 用户模板在添加时已做强制校验就检查过默认值
     */
    fun insert(map: Map<String, Any>): Int {
        val vo = UserUtil.toUser<UserVo>(map)
        val currentUser = TokenUtil.getCurrentUser()
        //后期改为如果是系统创建指定为超级管理员
        var cuid = 0L
        if (currentUser == null) {
            //预留主动注册
        } else {
            //必须是同一个应用，否则创建人为应用超管
            if (currentUser.appId == AppUtil.getAppId()) {
                cuid = currentUser.id!!
            }
            when (vo.type) {
                UserType.SYSTEM_SUPER_ADMIN.name -> throw CustomException(com.basicfu.sip.core.common.Enum.NOT_PERMISSION_ADD_SYSTEM_SUPER_ADMIN)
                UserType.SYSTEM_ADMIN.name ->
                    currentUser.type != UserType.SYSTEM_SUPER_ADMIN.name && throw CustomException(com.basicfu.sip.core.common.Enum.NOT_PERMISSION_ADD_SYSTEM_ADMIN)
                UserType.APP_SUPER_ADMIN.name -> throw CustomException(com.basicfu.sip.core.common.Enum.NOT_PERMISSION_ADD_APP_SYSTEM_ADMIN)
                UserType.APP_ADMIN.name ->
                    currentUser.type != UserType.SYSTEM_SUPER_ADMIN.name &&
                            currentUser.type != UserType.SYSTEM_ADMIN.name &&
                            currentUser.type != UserType.APP_SUPER_ADMIN.name &&
                            throw CustomException(com.basicfu.sip.core.common.Enum.NOT_PERMISSION_ADD_APP_ADMIN)
            //允许普通用户通过分配的角色添加普通用户
//                UserType.NORMAL.name->
//                    currentUser.type!=UserType.SYSTEM_SUPER_ADMIN.name&&
//                    currentUser.type!=UserType.SYSTEM_ADMIN.name&&
//                    currentUser.type!=UserType.APP_SUPER_ADMIN.name&&
//                    currentUser.type!=UserType.APP_ADMIN.name&&
//                            throw CustomException(com.basicfu.sip.core.common.Enum.NOT_PERMISSION_ADD_NORMAL)
            }
        }
        //检查用户名重复
        if (userAuthMapper.selectCount(generate {
                username = vo.username
            }) > 0) throw CustomException(Enum.User.EXIST_USER)
        //添加用户
        val user = dealInsert(generate<User> {
            username = vo.username
            nickname = vo.nickname
            content = dealUserTemplate(vo.content).toJSONString()
            type = vo.type
            this.cuid = cuid
        })
        mapper.insertSelective(user)
        //处理用户角色
        if (vo.roleIds != null) {
            com.basicfu.sip.client.util.UserUtil.updateRole(user.id!!, vo.roleIds!!)
        }
        //添加用户授权
        val userAuth = dealInsert(generate<UserAuth> {
            uid = user.id
            username = vo.username
            password = BCryptPasswordEncoder().encode(vo.username + vo.password)
            type = 0
        })
        userAuthMapper.insertSelective(userAuth)
        //mobile、phone特殊处理
        if (vo.mobile != null) {
            userAuthMapper.insertSelective(dealInsert(generate<UserAuth> {
                uid = user.id
                username = vo.mobile
                password = BCryptPasswordEncoder().encode(vo.mobile + vo.password)
                type = 1
            }))
        }
        if (vo.email != null) {
            userAuthMapper.insertSelective(dealInsert(generate<UserAuth> {
                uid = user.id
                username = vo.email
                password = BCryptPasswordEncoder().encode(vo.email + vo.password)
                type = 2
            }))
        }
        return 1
    }

    fun update(map: Map<String, Any>): Int {
        val vo = UserUtil.toUser<UserVo>(map)
        val currentUser = TokenUtil.getCurrentUser()!!
        //检查用户类型是否能变动
        val user = mapper.selectByPrimaryKey(vo.id) ?: return 0
        if (user.type != vo.type) {
            //超级管理员无法变更用户类型
            if (user.type == UserType.SYSTEM_SUPER_ADMIN.name || user.type == UserType.APP_SUPER_ADMIN.name) {
                throw CustomException(com.basicfu.sip.core.common.Enum.SUPER_ADMIN_NOT_CHANGE_USER_TYPE)
            }
            //如果发生系统/应用级别变化禁止
            if (UserType.valueOf(user.type!!).system != UserType.valueOf(vo.type!!).system) {
                throw CustomException(com.basicfu.sip.core.common.Enum.SYSTEM_USER_NOT_EXCHANGE_APP_USER)
            }
            when (vo.type) {
                UserType.SYSTEM_SUPER_ADMIN.name -> throw CustomException(com.basicfu.sip.core.common.Enum.NOT_PERMISSION_ADD_SYSTEM_SUPER_ADMIN)
                UserType.SYSTEM_ADMIN.name ->
                    currentUser.type != UserType.SYSTEM_SUPER_ADMIN.name && throw CustomException(com.basicfu.sip.core.common.Enum.NOT_PERMISSION_ADD_SYSTEM_ADMIN)
                UserType.APP_SUPER_ADMIN.name -> throw CustomException(com.basicfu.sip.core.common.Enum.NOT_PERMISSION_ADD_APP_SYSTEM_ADMIN)
                UserType.APP_ADMIN.name ->
                    currentUser.type != UserType.SYSTEM_SUPER_ADMIN.name &&
                            currentUser.type != UserType.SYSTEM_ADMIN.name &&
                            currentUser.type != UserType.APP_SUPER_ADMIN.name &&
                            throw CustomException(com.basicfu.sip.core.common.Enum.NOT_PERMISSION_ADD_APP_ADMIN)
            }
        }
        //检查用户名重复
        val checkUsername = mapper.selectOne(generate {
            username = vo.username
        })
        if (checkUsername != null && checkUsername.id != vo.id) throw CustomException(Enum.User.EXIST_USER)
        //更新用户内容
        mapper.updateByPrimaryKeySelective(dealUpdate(generate<User> {
            id = vo.id
            type = vo.type
            content = dealUserTemplate(vo.content).toJSONString()
            nickname = vo.nickname
        }))
        //处理用户角色
        if (vo.roleIds != null) {
            com.basicfu.sip.client.util.UserUtil.updateRole(vo.id!!, vo.roleIds!!)
        }
        //更新用户授权
        //判断用户密码和上次是否一致，不一致进行更新
        val userAuths = userAuthMapper.select(generate {
            uid = vo.id
        }).associateBy({ it.type }, { it })
        val usernameAuth = userAuths[0]!!
        val mobileAuth = userAuths[1]
        val emailAuth = userAuths[2]
        if (!vo.password.isNullOrBlank() && !PasswordUtil.matches(
                vo.username + vo.password!!,
                usernameAuth.password!!
            )
        ) {
            userAuthMapper.updateByPrimaryKeySelective(dealUpdate(generate<UserAuth> {
                id = usernameAuth.id
                password = BCryptPasswordEncoder().encode(vo.username + vo.password)
            }))
        }
        //mobile、phone特殊处理
        if (vo.mobile != null) {
            if (mobileAuth == null) {
                userAuthMapper.insertSelective(dealInsert(generate<UserAuth> {
                    uid = vo.id
                    username = vo.mobile
                    password = BCryptPasswordEncoder().encode(vo.mobile + vo.password)
                    type = 1
                }))
            } else if (vo.mobile != mobileAuth.username) {
                userAuthMapper.updateByPrimaryKeySelective(dealUpdate(generate<UserAuth> {
                    id = mobileAuth.id
                    username = vo.mobile
                    password = BCryptPasswordEncoder().encode(vo.mobile + vo.password)
                }))
            }
        } else if (mobileAuth != null) {
            userAuthMapper.deleteByPrimaryKey(mobileAuth.id)
        }
        if (vo.email != null) {
            if (emailAuth == null) {
                userAuthMapper.insertSelective(dealInsert(generate<UserAuth> {
                    uid = vo.id
                    username = vo.email
                    password = BCryptPasswordEncoder().encode(vo.email + vo.password)
                    type = 2
                }))
            } else if (vo.email != emailAuth.username) {
                userAuthMapper.updateByPrimaryKeySelective(dealUpdate(generate<UserAuth> {
                    id = emailAuth.id
                    username = vo.email
                    password = BCryptPasswordEncoder().encode(vo.email + vo.password)
                }))
            }
        } else if (emailAuth != null) {
            userAuthMapper.deleteByPrimaryKey(emailAuth.id)
        }
        return 1
    }

    fun delete(ids: List<Long>?): Int {
        return deleteByIds(ids)
    }

    /**
     * 处理返回的用户，创建人、邮箱、手机、最后登录时间、角色
     */
    fun dealReturnUser(users: List<UserDto>) {
        if (users.isNotEmpty()) {
            val userIds = users.map { it.id!! }
            val createUserMap = selectByIds(users.map { it.cuid!! }).associateBy { it.id }
            val listRoleByIds = com.basicfu.sip.client.util.UserUtil.listRoleByIds(userIds)
            users.forEach {
                it.roles = listRoleByIds[it.id]
                it.cuname = createUserMap[it.cuid]?.nickname ?: "系统"
            }
            val userAuths = userAuthMapper.selectByExample(example<UserAuth> {
                andIn(UserAuth::uid, userIds)
            }).groupBy({ it.uid }, { it })
            users.forEach {
                val userAuth = userAuths[it.id]
                if (userAuth != null) {
                    val userAuthMap = userAuth.associateBy({ it.type!! }, { it })
                    it.mobile = userAuthMap[1]?.username
                    it.email = userAuthMap[2]?.username
                    it.ldate = userAuth.map { it.ldate!! }.max()
                }
            }
        }
    }

    /**
     * 处理json串内容
     */
    fun dealUserTemplate(contentJson: JSONObject): JSONObject {
        //获取该租户下的用户模板信息,传过来空值也要根据模板处理默认值
        val userTemplateList = userTemplateService.all()
        val contentResult = JSONObject()
        userTemplateList.forEach { it ->
            val extra = it.extra!!
            val enName = it.enName!!
            val required = it.required!!
            val name = it.name!!
            val defaultValue = it.defaultValue!!
            val value = contentJson.getString(enName)
            when (valueOf(it.type!!)) {
                TEXT -> {
                    if (value == null) {
                        if (required) {
                            throw CustomException("字段名[$name]不能为空")
                        }
                        contentResult[enName] = defaultValue
                    } else {
                        val extraArray = extra.split("~")
                        val startLength = extraArray[0].toInt()
                        val endLength = extraArray[1].toInt()
                        if (value.length !in startLength..endLength) {
                            throw CustomException("字段名[$name]长度需要[$startLength~$endLength]个字符")
                        }
                        contentResult[enName] = value
                    }
                }
                NUMBER -> {
                    if (value == null) {
                        if (required) {
                            throw CustomException("字段名[$name]不能为空")
                        }
                        contentResult[enName] = defaultValue
                    } else {
                        val extraArray = extra.split("&")
                        val lengthRange = extraArray[0].split(",")
                        val valueRange = extraArray[1].split("~")
                        if (!value.isNullOrBlank()) {
                            val startLength = lengthRange[0].toInt()
                            val endLength = lengthRange[1].toInt()
                            val startValue = valueRange[0].toFloat()
                            val endValue = valueRange[1].toFloat()
                            val splitValue = value.split(".")
                            if (splitValue.size == 1) {
                                //移除符号判断长度
                                if (Math.abs(splitValue[0].toLong()).toString().length !in 1..startLength) {
                                    throw CustomException("字段名[$name]整数位需要[1~$startLength]位")
                                }
                                if (splitValue[0].toLong() !in startValue..endValue) {
                                    throw CustomException(
                                        "字段名[$name]值范围需要[${BigDecimal(startValue.toString()).setScale(
                                            endLength
                                        )}~${BigDecimal(endValue.toString()).setScale(endLength)}]之间"
                                    )
                                }
                                contentResult[enName] = splitValue[0]
                            }
                            if (splitValue.size == 2) {
                                if (Math.abs(splitValue[0].toLong()).toString().length !in 1..startLength) {
                                    throw CustomException("字段名[$name]整数位需要[1~$startLength]位")
                                }
                                if (splitValue[1].length > endLength) {
                                    throw CustomException("字段名[$name]小数位不能大于$endLength]位")
                                }
                                //小数为不够自动补0
                                val floatValue = BigDecimal(value).setScale(endLength).toFloat()
                                if (floatValue !in startValue..endValue) {
                                    throw CustomException(
                                        "字段名[$name]值范围需要[${BigDecimal(startValue.toString()).setScale(
                                            endLength
                                        )}~${BigDecimal(endValue.toString()).setScale(endLength)}]之间"
                                    )
                                }
                                contentResult[enName] = floatValue
                            }
                        }
                    }
                }
            //不支持默认值、必选
                CHECK -> {
                    if (value == null) {
                        contentResult[enName] = "[]"
                    } else {
                        val dictMap = DictUtil.getMap(extra)
                        JSON.parseArray(value).forEach { item ->
                            if (!dictMap.containsKey(item.toString())) {
                                throw CustomException("找不到字典[$extra]下为[$item]的值")
                            }
                        }
                        contentResult[enName] = value
                    }
                }
            //不支持默认值、必选
                RADIO -> {
                    if (value == null) {
                        contentResult[enName] = ""
                    } else {
                        val dictMap = DictUtil.getMap(extra)
                        if (!dictMap.containsKey(value.toString())) {
                            throw CustomException("找不到字典[$extra]下为[$value]的值")
                        }
                        contentResult[enName] = value
                    }
                }
            //下拉不支持默认值，支持必选
                SELECT -> {
                    if (value == null) {
                        if (required) {
                            throw CustomException("字段名[$name]不能为空")
                        }
                        contentResult[enName] = ""
                    } else {
                        val dictMap = DictUtil.getMap(extra)
                        if (!dictMap.containsKey(value.toString())) {
                            throw CustomException("找不到字典[$extra]下为[$value]的值")
                        }
                        contentResult[enName] = value
                    }
                }
                DATE -> {
                    if (value == null) {
                        if (required) {
                            throw CustomException("字段名[$name]不能为空")
                        }
                        contentResult[enName] = ""
                    } else {
                        val sdf = SimpleDateFormat(extra)
                        contentResult[enName] = sdf.format(sdf.parse(value))
                    }
                }
            }
        }
        return contentResult
    }
}
