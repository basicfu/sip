package com.basicfu.sip.client.controller

import com.alibaba.fastjson.JSONArray
import com.basicfu.sip.client.model.Result
import com.basicfu.sip.client.util.ApiUtil
import org.bouncycastle.cms.RecipientId.password
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.LocalVariableTableParameterNameDiscoverer
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import java.util.*

/**
 * @author basicfu
 * @date 2018/7/8
 */
@RestController("sipClientController")
class SipController {
    @Autowired
    lateinit var requestMappingHandlerMapping: RequestMappingHandlerMapping

    @PostMapping("/login")
    fun login(@RequestParam username: String, @RequestParam password: String): Result<Any> {
        return Result(ApiUtil.login(username, password))
    }

    @PostMapping("/register")
    fun register(@RequestBody map: Map<String, Any>): Result<Any> {
        return Result(ApiUtil.register(map))
    }

    @GetMapping("/logout")
    fun logout(): Result<Any> {
        return Result(ApiUtil.logout())
    }

    @GetMapping("/sip/client/url")
    fun getServiceAllUrl(): Result<Any> {
        val result = JSONArray()
        val map = requestMappingHandlerMapping.handlerMethods
        for (m in map.entries) {
            val hashMap = HashMap<String, Any>()
            val info = m.key
            val handlerMethod = m.value
            val methodName = handlerMethod.method.name
            hashMap["url"] = info.patternsCondition.patterns
            hashMap["requestMethod"] = info.methodsCondition.methods
            hashMap["className"] = handlerMethod.method.declaringClass.name
            hashMap["methodName"] = methodName
            hashMap["returnClassName"] = handlerMethod.method.returnType.name
            hashMap["parameterType"] = handlerMethod.method.parameterTypes
            if (hashMap["url"].toString() != "[/error]") {
                hashMap["parameterName"] =
                    LocalVariableTableParameterNameDiscoverer().getParameterNames(handlerMethod.method)!!
                result.add(hashMap)
            }

        }
        return Result(result)
    }
}
