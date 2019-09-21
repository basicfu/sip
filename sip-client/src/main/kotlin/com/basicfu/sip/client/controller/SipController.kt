package com.basicfu.sip.client.controller

import com.alibaba.fastjson.JSONArray
import com.basicfu.sip.client.model.Result
import com.basicfu.sip.client.util.ApiUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.LocalVariableTableParameterNameDiscoverer
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import java.util.*

@RestController("sipClientController")
@RequestMapping("/sip")
class SipController {
    @Autowired
    lateinit var requestMappingHandlerMapping: RequestMappingHandlerMapping

    @GetMapping("/user")
    fun user(): Any {
        return ApiUtil.user()
    }

    @PostMapping("/login")
    fun login(@RequestBody map: Map<String, Any>): Any {
        return ApiUtil.login(map)
    }

    @PostMapping("/register")
    fun register(@RequestBody map: Map<String, Any>): Any {
        return ApiUtil.register(map)
    }

    @GetMapping("/logout")
    fun logout(): Any {
        return ApiUtil.logout()
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
