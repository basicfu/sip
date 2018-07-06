package com.basicfu.sip.base.service

import com.basicfu.sip.base.common.Enum
import com.basicfu.sip.base.mapper.UserMapper
import com.basicfu.sip.base.model.dto.UserDto
import com.basicfu.sip.base.model.po.User
import com.basicfu.sip.base.model.po.UserAuth
import com.basicfu.sip.base.model.vo.UserVo
import com.basicfu.sip.core.exception.CustomException
import com.basicfu.sip.core.mapper.example
import com.basicfu.sip.core.mapper.generate
import com.basicfu.sip.core.service.BaseService
import com.github.pagehelper.PageInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

/**
 * @author basicfu
 * @date 2018/6/30
 */
@Service
class UserService : BaseService<UserMapper, User>() {
    @Autowired
    lateinit var userAuthService: UserAuthService

    fun list(vo: UserVo): PageInfo<UserDto> {
        val list = selectPage<UserDto>(example<User> {
            andLike {
                username = vo.username
            }
        })
        return list
    }

    fun get(vo: UserVo): UserDto {
        return to(mapper.selectOne(generate {
        username=vo.username
    }))
    }

    fun suggest(vo: UserVo):List<UserDto> {
        val list=to<UserDto>(mapper.selectByExample(example<User>{
            andLike {
                username=vo.username
            }
        }))
        return list
    }

    /**
     * 用户名注册
     * 获取模板信息判断格式是否正确
     * 添加用户、用户授权表
     */
    fun insert(vo: UserVo): Int {
        val usernameCount = userAuthService.mapper.selectCount(generate {
            username = vo.username
        })
        if (usernameCount > 0) {
            throw CustomException(Enum.User.EXIST_USER)
        }
        val user = dealInsert(generate<User> {
            username=vo.username
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
