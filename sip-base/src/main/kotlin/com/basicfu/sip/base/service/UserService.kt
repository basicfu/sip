package com.basicfu.sip.base.service

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.basicfu.sip.base.common.Enum
import com.basicfu.sip.base.common.Enum.FieldType.*
import com.basicfu.sip.base.mapper.UserAuthMapper
import com.basicfu.sip.base.mapper.UserMapper
import com.basicfu.sip.base.model.po.User
import com.basicfu.sip.base.model.po.UserAuth
import com.basicfu.sip.base.model.vo.UserVo
import com.basicfu.sip.base.util.PasswordUtil
import com.basicfu.sip.client.util.DictUtil
import com.basicfu.sip.core.common.Constant
import com.basicfu.sip.core.common.exception.CustomException
import com.basicfu.sip.core.common.mapper.example
import com.basicfu.sip.core.common.mapper.generate
import com.basicfu.sip.core.model.dto.UserDto
import com.basicfu.sip.core.model.po.Resource
import com.basicfu.sip.core.service.BaseService
import com.basicfu.sip.core.util.RedisUtil
import com.basicfu.sip.core.util.TokenUtil
import com.basicfu.sip.core.util.UserUtil
import com.github.pagehelper.PageInfo
import org.apache.ibatis.session.RowBounds
import org.bouncycastle.asn1.x500.style.RFC4519Style.uid
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

    fun get(id: Long): JSONObject? {
        val result=to<UserDto>(mapper.selectByPrimaryKey(id))
        result?.let {
            val userAuths = userAuthMapper.select(generate {
                uid=id
            })
            val userAuthMap=userAuths.associateBy({ it.type }, { it })
            it.mobile=userAuthMap[1]?.username
            it.email=userAuthMap[2]?.username
            it.ldate=userAuths.map { it.ldate!! }.max()
        }
        return UserUtil.toJson(result)
    }

    fun getCurrentUser(): JSONObject? {
        var user = TokenUtil.getCurrentUser()
        if (user == null) {
            user = TokenUtil.getGuestUser()
        }
        return UserUtil.toJson(user)
    }

    fun list(vo: UserVo): PageInfo<JSONObject> {
        val pageList = selectPage<UserDto>(example<User> {
            andLike {
                username = vo.username
            }
        })
        val users=pageList.list
        if(users.isNotEmpty()){
            val userAuths = userAuthMapper.selectByExample(example<UserAuth> {
                andIn(UserAuth::uid,users.map { it.id })
            }).groupBy({ it.uid }, { it })
            users.forEach {
                val userAuth = userAuths[it.id]
                if (userAuth != null) {
                    val userAuthMap = userAuth.associateBy({ it.type!! }, { it })
                    it.mobile = userAuthMap[1]?.username
                    it.email = userAuthMap[2]?.username
                    it.ldate=userAuth.map { it.ldate!! }.max()
                }
            }
        }
        val result = PageInfo<JSONObject>()
        BeanUtils.copyProperties(pageList, result)
        result.list = UserUtil.toJson(users)
        return result
    }

    fun listByIds(ids: List<Long>): List<JSONObject>? {
        val users = to<UserDto>(mapper.selectByExample(example<User> {
            andIn(User::id, ids)
        }))
        if(users.isNotEmpty()){
            val userAuths = userAuthMapper.selectByExample(example<UserAuth> {
                andIn(UserAuth::uid,users.map { it.id })
            }).groupBy({ it.uid }, { it })
            users.forEach {
                val userAuth = userAuths[it.id]
                if (userAuth != null) {
                    val userAuthMap = userAuth.associateBy({ it.type!! }, { it })
                    it.mobile = userAuthMap[1]?.username
                    it.email = userAuthMap[2]?.username
                    it.ldate=userAuth.map { it.ldate!! }.max()
                }
            }
        }
        return UserUtil.toJson(users)
    }

    fun listUsernameByIds(ids: List<Long>): List<JSONObject>? {
        val users = to<UserDto>(mapper.selectByExample(example<User> {
            select(User::id, User::username)
            andIn(User::id, ids)
        }))
        return UserUtil.toJson(users)
    }

    fun suggest(q: String, size: Int): List<JSONObject>? {
        //可测试性能
//        mapper.selectBySql("SELECT u.id,u.tenant_id,u.content,u.cdate,u.udate,status,ua.username,ua.type from user u LEFT JOIN user_auth ua on u.id=ua.uid INNER JOIN (select DISTINCT(uid) from user_auth WHERE username like '%1%' limit 10) as sub on u.id=sub.uid;")
        val userIds = userAuthMapper.selectByExampleAndRowBounds(example<UserAuth> {
            select(UserAuth::uid)
            distinct()
            andLike {
                username = q
            }
        }, RowBounds(0, size)).mapNotNull { it.uid }
        val users = to<UserDto>(selectByIds(userIds))
        if (users.isNotEmpty()) {
            val userAuths = userAuthMapper.selectByExample(example<UserAuth> {
                select(UserAuth::uid, UserAuth::type, UserAuth::username, UserAuth::ldate)
                andIn(UserAuth::uid, users.map { it.id })
            }).groupBy({ it.uid }, { it })
            users.forEach {
                val userAuth = userAuths[it.id]
                if (userAuth != null) {
                    val userAuthMap = userAuth.associateBy({ it.type!! }, { it })
                    it.mobile = userAuthMap[1]?.username
                    it.email = userAuthMap[2]?.username
                    it.ldate=userAuth.map { it.ldate!! }.max()
                }
            }
        }
        return UserUtil.toJson(users)
    }

    /**
     * 登录
     * 用户名登录
     * 后期密码使用加密后的值
     * TODO 登录后清除该用户其他token
     * TODO 校验手机和邮箱拥有者后可开启手机或邮箱登录
     * TODO 登录查询表过多，可优化连为连表查询
     */
    fun login(vo: UserVo): JSONObject? {
        val userAuth = userAuthMapper.selectOne(generate {
            username = vo.username
            type = 0
        }) ?: throw CustomException(Enum.User.USERNAME_OR_PASSWORD_ERROR)
        if (!PasswordUtil.matches(vo.username + vo.password!!, userAuth.password!!)) throw CustomException(
            Enum.User.USERNAME_OR_PASSWORD_ERROR
        )
        val user = to<UserDto>(mapper.selectByPrimaryKey(userAuth.uid))
        val currentTime = (System.currentTimeMillis() / 1000).toInt()
        userAuthMapper.updateByPrimaryKeySelective(generate {
            id = userAuth.id
            ldate = currentTime
        })
        val permission =
            com.basicfu.sip.client.util.UserUtil.getPermissionByUidJson(user!!.id!!) ?: throw CustomException(
                Enum.User.LOGIN_ERROR
            )
        val userAuths = userAuthMapper.select(generate {
            uid = userAuth.uid
        }).associateBy({ it.type }, { it })
        user.mobile = userAuths[1]?.username
        user.email = userAuths[2]?.username
        user.ldate = currentTime
        user.roles = permission.getJSONArray("roles")
        user.menus = permission.getJSONArray("menus")
        user.permissions = permission.getJSONArray("permissions")
        user.resources = permission.getJSONArray("resources").toJavaList(Resource::class.java)
            .groupBy({ it.serviceId.toString() }, { "/" + it.method + it.url })
        val token = TokenUtil.generateToken()
        //TODO 系统设置登录过期时间
        RedisUtil.set(
            Constant.Redis.TOKEN_PREFIX + token, user,
            Constant.System.SESSION_TIMEOUT
        )
        val result = JSONObject()
        result["token"] = token
        result["time"] = System.currentTimeMillis() / 1000
        result["username"] = user.username
        result["roles"] = user.roles
        return result
    }

    /**
     * 登出
     */
    fun logout() {
        TokenUtil.getCurrentToken()?.let {
            RedisUtil.del(Constant.Redis.TOKEN_PREFIX + it)
        }
    }

    /**
     * 用户模板需要在添加时强制限制好格式
     */
    fun insert(vo: UserVo): Int {
        //检查用户名重复
        if (userAuthMapper.selectCount(generate {
                username = vo.username
            }) > 0) throw CustomException(Enum.User.EXIST_USER)
        val contentJson = vo.content
        //获取该租户下的用户模板信息,传过来空值也要根据模板处理默认值
        val userTemplateList = userTemplateService.all()
        val contentResult = JSONObject()
        //mobile、phone特殊处理
        val mobile = contentJson.getString(UserDto::mobile.name)
        val email = contentJson.getString(UserDto::email.name)
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
                                throw CustomException("字段名[$name]值范围需要[$startValue~$endValue]之间")
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
                                throw CustomException("字段名[$name]值范围需要[$startValue~$endValue]之间")
                            }
                            contentResult[enName] = floatValue
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
        val user = dealInsert(generate<User> {
            username = vo.username
            content = contentResult.toJSONString()
            type = 2
        })
        mapper.insertSelective(user)
        val userAuth = dealInsert(generate<UserAuth> {
            uid = user.id
            username = vo.username
            password = BCryptPasswordEncoder().encode(vo.username + vo.password)
            type = 0
        })
        userAuthMapper.insertSelective(userAuth)
        if (mobile != null) {
            userAuthMapper.insertSelective(dealInsert(generate<UserAuth> {
                uid = user.id
                username = mobile
                password = BCryptPasswordEncoder().encode(vo.username + vo.password)
                type = 1
            }))
        }
        if (email != null) {
            userAuthMapper.insertSelective(dealInsert(generate<UserAuth> {
                uid = user.id
                username = email
                password = BCryptPasswordEncoder().encode(vo.username + vo.password)
                type = 2
            }))
        }
        return 1
    }

    fun update(vo: UserVo): Int {
        return 0
    }

    fun delete(ids: List<Long>?): Int {
        return deleteByIds(ids)
    }
}
