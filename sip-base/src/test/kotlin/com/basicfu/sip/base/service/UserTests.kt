package com.basicfu.sip.base.service

import com.basicfu.sip.base.model.vo.UserVo
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

/**
 * @author basicfu
 * @date 2018/6/30
 */
@RunWith(SpringRunner::class)
@SpringBootTest
class UserTests {
    @Autowired
    lateinit var userService: UserService

    @Test
    fun insert() {
        val vo = UserVo()
        vo.username = "basicfu"
        vo.password = "123456"
        Assert.assertEquals(userService.insert(vo), 1)
    }
}
