package com.basicfu.sip.base.service

import com.basicfu.sip.base.BaseTests
import com.basicfu.sip.base.mapper.UserMapper
import com.basicfu.sip.base.model.po.User
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author basicfu
 * @date 2018/6/30
 */
class UserTests:BaseTests<UserMapper,User>(){
    @Autowired
    lateinit var userService: UserService

    fun preInsert():Long{
//        val vo= generate<UserVo> {
//            username="basicfu"
//            password="123456"
//            mobile="18611110000"
//            email="basicfu@gmail.com"
//        }
//        Assert.assertEquals(1, userService.insert(vo))
        return mapper.selectLastInsertId()
    }

    @Test
    fun test(){
        assert(true)
    }
}
