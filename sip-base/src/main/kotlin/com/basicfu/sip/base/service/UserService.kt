package com.basicfu.sip.base.service

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.basicfu.sip.base.common.Enum
import com.basicfu.sip.base.common.Enum.FieldType.*
import com.basicfu.sip.base.mapper.UserMapper
import com.basicfu.sip.base.model.dto.UserDto
import com.basicfu.sip.base.model.po.User
import com.basicfu.sip.base.model.po.UserAuth
import com.basicfu.sip.base.model.vo.UserTemplateVo
import com.basicfu.sip.base.model.vo.UserVo
import com.basicfu.sip.base.util.PasswordUtil
import com.basicfu.sip.client.util.DictUtil
import com.basicfu.sip.core.exception.CustomException
import com.basicfu.sip.core.mapper.example
import com.basicfu.sip.core.mapper.generate
import com.basicfu.sip.core.service.BaseService
import com.basicfu.sip.core.util.RedisUtil
import com.basicfu.sip.core.util.TokenUtil
import com.github.pagehelper.PageInfo
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
    lateinit var userAuthService: UserAuthService
    @Autowired
    lateinit var userTemplateService: UserTemplateService

    fun list(vo: UserVo): PageInfo<UserDto> {
        val list = selectPage<UserDto>(example<User> {
            andLike {
                username = vo.username
            }
        })
        return list
    }

    fun get(id: Long): UserDto? {
        return to(mapper.selectByPrimaryKey(id))
    }

    fun getByToken(token: String): UserDto? {
        return RedisUtil.get<UserDto>(token)
    }

    fun listUsernameByIds(ids: List<Long>): List<UserDto> {
        return to(mapper.selectByExample(example<User> {
            select(User::id,User::username)
            andIn(User::id,ids)
        }))
    }
    fun suggest(vo: UserVo): List<UserDto> {
        val list = to<UserDto>(mapper.selectByExample(example<User> {
            andLike {
                username = vo.username
            }
        }))
        return list
    }

    /**
     * 登录
     * 用户名登录
     * 后期密码使用加密后的值
     * //TODO 登录成功获取用户资源放入缓存
     */
    fun login(vo: UserVo): JSONObject? {
        val userAuth= userAuthService.mapper.selectOne(generate {
            username=vo.username
            type=0
        }) ?: throw CustomException(Enum.User.USERNAME_OR_PASSWORD_ERROR)
        if(!PasswordUtil.matches(vo.password!!,userAuth.password!!))throw CustomException(Enum.User.USERNAME_OR_PASSWORD_ERROR)
        val user=to<UserDto>(mapper.selectByPrimaryKey(userAuth.uid))
        userAuthService.mapper.updateByPrimaryKeySelective(generate {
            id=userAuth.id
            ldate=(System.currentTimeMillis() / 1000).toInt()
        })
        RedisUtil.set(TokenUtil.generateToken(),user)
        val result = JSONObject()
        result["success"] = true
        return result
        //查询用户资源
//        val rids = urMapper.selectByIds(user.id.toString()).mapNotNull { it.roleId }
//        val mids = if (rids.isEmpty()) emptyList() else rmMapper.selectByIds(StringUtils.join(rids, ",")).mapNotNull { it.menuId }
//        val pids = if (rids.isEmpty()) emptyList() else rpMapper.selectByIds(StringUtils.join(rids, ",")).mapNotNull { it.permissionId }
//        val resourceIds = if (pids.isEmpty()) emptyList() else prMapper.selectByIds(StringUtils.join(pids, ",")).mapNotNull { it.resourceId }
//        val tokenStr = Utils.generateToken()
    }
    /**
     * 用户模板需要在添加时强制限制好格式
     */
    fun insert(vo: UserVo): Int {
        //检查用户名重复
        if (userAuthService.mapper.selectCount(generate {
                username = vo.username
            }) > 0) throw CustomException(Enum.User.EXIST_USER)
        val contentJson = vo.content
        //获取该租户下的用户模板信息,传过来空值也要根据模板处理默认值
        val userTemplateList = userTemplateService.all(UserTemplateVo())
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
                    if(value==null){
                        contentResult[enName]="[]"
                    }else{
                        val dictMap = DictUtil.getMap(extra) ?: throw CustomException("找不到字典[$name]请确认是否存在")
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
                    if(value==null){
                        contentResult[enName] = ""
                    }else{
                        val dictMap = DictUtil.getMap(extra) ?: throw CustomException("找不到字典[$name]请确认是否存在")
                        if (!dictMap.containsKey(value.toString())) {
                            throw CustomException("找不到字典[$extra]下为[$value]的值")
                        }
                        contentResult[enName] = value
                    }
                }
                //下拉不支持默认值，支持必选
                SELECT -> {
                    if (value == null) {
                        if(required){
                            throw CustomException("字段名[$name]不能为空")
                        }
                        contentResult[enName] = ""
                    }else{
                        val dictMap = DictUtil.getMap(extra) ?: throw CustomException("找不到字典[$name]请确认是否存在")
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
                    }else{
                        val sdf=SimpleDateFormat(extra)
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
        userAuthService.mapper.insertSelective(userAuth)
        return 1
    }

    fun update(vo: UserVo): Int {
        return 0
    }

    fun delete(ids: List<Long>?): Int {
        return deleteByIds(ids)
    }
}
