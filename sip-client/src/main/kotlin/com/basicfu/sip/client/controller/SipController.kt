package com.basicfu.sip.client.controller

import com.alibaba.fastjson.JSONArray
import com.basicfu.sip.client.model.Result
import com.basicfu.sip.client.util.SipUtil
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
        return SipUtil.userController()
    }

    @PostMapping("/sms/{mobile}")
    fun sms(@PathVariable mobile: String): Any {
        return SipUtil.smsController(mobile)
    }

    @PostMapping("/login")
    fun login(@RequestBody map: Map<String, Any>): Any {
        return SipUtil.loginController(map)
    }

    @PostMapping("/register")
    fun register(@RequestBody map: Map<String, Any>): Any {
        return SipUtil.registerController(map)
    }

    @GetMapping("/logout")
    fun logout(): Any {
        return SipUtil.logoutController()
    }

    @PostMapping("/update/password")
    fun updatePassword(@RequestBody map: Map<String, Any>): Any {
        return SipUtil.updatePasswordController(map)
    }

    @PostMapping("/find/password")
    fun findPassword(@RequestBody map: Map<String, Any>): Any {
        return SipUtil.findPasswordController(map)
    }

    @PostMapping("/update")
    fun updateUser(@RequestBody map: Map<String, Any>): Any {
        return SipUtil.updateUserController(map)
    }

    @DeleteMapping("/delete")
    fun deleteUserController(@RequestBody ids: List<String>): Any {
        return SipUtil.deleteUserController(ids)
    }

    @DeleteMapping("/delete/phy")
    fun deleteUserPhyController(@RequestBody ids: List<String>): Any {
        return SipUtil.deleteUserPhyController(ids)
    }

    @GetMapping("/role/all")
    fun deleteUserPhyController(): Any {
        return SipUtil.allRoleController()
    }


    @GetMapping("/sync/url")
    fun syncUrl(): Result<Any> {
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
