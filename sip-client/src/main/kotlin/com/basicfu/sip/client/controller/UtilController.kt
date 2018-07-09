package com.basicfu.sip.client.controller

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
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

    @GetMapping("/client/url","/test/tttt")
    fun getUrl() {
        val urlList = ArrayList<HashMap<String, Any>>()
        val map = requestMappingHandlerMapping.handlerMethods
        for (m in map.entries) {
            val hashMap = HashMap<String, Any>()
            val info = m.key
            val handlerMethod = m.value
            val methodName = handlerMethod.method.name
            hashMap["url"] = info.patternsCondition.patterns
            hashMap["type"] = info.methodsCondition.methods
            hashMap["className"] = handlerMethod.method.declaringClass.name
            hashMap["method"] = methodName
            hashMap["returnClassName"] = handlerMethod.method.returnType.name
            hashMap["parameter"] = handlerMethod.method.parameterTypes
            urlList.add(hashMap)
            println(JSON.toJSONString(hashMap))
            val parameterNames =
                LocalVariableTableParameterNameDiscoverer().getParameterNames(handlerMethod.method)
            val jsonObject = JSONObject()
            if (handlerMethod.method.parameterTypes != null) {
                val parameterTypes = handlerMethod.method.parameterTypes
                for (parameterType in parameterTypes) {
                    if (parameterType.name.endsWith("Vo")) {
                        val fields = parameterType.declaredFields
                        for (field in fields) {
                            jsonObject[field.name] = ""
                        }
                    }
                }
            }
//            System.out.println(jsonObject.toJSONString())
        }
    }
}
