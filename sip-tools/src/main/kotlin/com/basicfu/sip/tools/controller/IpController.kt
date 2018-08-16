package com.basicfu.sip.tools.controller

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.serializer.SerializerFeature
import com.basicfu.sip.tools.util.IpUtil
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest


/**
 * @author basicfu
 * @date 2018/8/15
 */
@RestController
@RequestMapping("/ip")
class IpController {

    @GetMapping
    fun default(request:HttpServletRequest): String {
        return IpUtil.getIpAddress(request)+"\n"
    }
    @GetMapping("/info")
    fun info(request:HttpServletRequest):String{
        val result=JSONObject()
        result["ip"] = IpUtil.getIpAddress(request)
        result["accept"] = request.getHeader("accept")
        result["accept-encoding"] = request.getHeader("accept-encoding")
        result["accept-language"] = request.getHeader("accept-language")
        result["cache-control"] = request.getHeader("cache-control")
        result["connection"] = request.getHeader("connection")
        result["cookie"] = request.getHeader("cookie")
        result["dnt"] = request.getHeader("dnt")
        result["host"] = request.getHeader("api.basicfu.com")//proxy
        result["upgrade-insecure-requests"] = request.getHeader("upgrade-insecure-requests")
        result["user-agent"] = request.getHeader("user-agent")
        return JSON.toJSONString(result, SerializerFeature.WriteMapNullValue)+"\n"
    }
}
