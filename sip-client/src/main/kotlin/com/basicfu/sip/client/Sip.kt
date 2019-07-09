package com.basicfu.sip.client

import com.alibaba.fastjson.JSON
import com.basicfu.sip.client.common.Constant
import com.basicfu.sip.client.util.HttpUtil
import com.basicfu.sip.client.util.RequestUtil
import org.omg.CORBA.Object

object Sip {
    fun register(any:Object){
        HttpUtil.post(Constant.REGISTER,any)
    }
    inline fun <reified T> getUser(): T? {
        return RequestUtil.getHeader(Constant.AUTHORIZATION)?.let { getUserByToken(it) }

    }
    inline fun <reified T> getUserByToken(token:String): T? {
        val json=JSON.parseObject(HttpUtil.get(Constant.GET_USER_BY_TOKEN,"token=$token"))
        return JSON.toJavaObject(json,T::class.java)
    }
}

fun main() {

}
