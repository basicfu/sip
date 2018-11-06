package com.basicfu.sip.client.controller

import com.alibaba.fastjson.JSONArray
import com.basicfu.sip.client.model.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.LocalVariableTableParameterNameDiscoverer
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import java.util.*

/**
 * @author basicfu
 * @date 2018/7/8
 */
@RestController
class UtilController {
    @Autowired
    lateinit var requestMappingHandlerMapping: RequestMappingHandlerMapping

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
                        LocalVariableTableParameterNameDiscoverer().getParameterNames(handlerMethod.method)
                result.add(hashMap)
            }

        }
        return Result(result)
    }
}
