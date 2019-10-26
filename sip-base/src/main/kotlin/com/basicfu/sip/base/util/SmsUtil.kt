package com.basicfu.sip.base.util

import com.alibaba.fastjson.JSON
import com.aliyuncs.CommonRequest
import com.aliyuncs.DefaultAcsClient
import com.aliyuncs.exceptions.ClientException
import com.aliyuncs.exceptions.ServerException
import com.aliyuncs.http.MethodType
import com.aliyuncs.profile.DefaultProfile
import com.basicfu.sip.base.common.config.AliyunConfig
import com.basicfu.sip.base.common.constant.Constant
import com.basicfu.sip.core.common.exception.CustomException
import com.basicfu.sip.core.util.RedisUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
object SmsUtil {

    fun send(mobile: String) {
        //根据配置文件取值，所以此处实时创建
        val profile: DefaultProfile =
            DefaultProfile.getProfile("default", AliyunConfig.AccessId, AliyunConfig.AccessKey)
        val code = ((Math.random() * 9 + 1) * 100000).toInt().toString()//随机6位验证码
        println(code)
        val client = DefaultAcsClient(profile)
        val request = CommonRequest()
        request.sysMethod = MethodType.POST
        request.sysDomain = "dysmsapi.aliyuncs.com"
        request.sysVersion = "2017-05-25"
        request.sysAction = "SendSms"
        request.putQueryParameter("RegionId", "default")
        request.putQueryParameter("PhoneNumbers", mobile)
        request.putQueryParameter("SignName", "初妆")
        request.putQueryParameter("TemplateCode", "SMS_174270504")
        request.putQueryParameter("TemplateParam", "{'code':\"$code\"}")
        val response = client.getCommonResponse(request)
        val json = JSON.parseObject(response.data)
        when (json.getString("Code")) {
            "OK" -> {}
            "isv.BUSINESS_LIMIT_CONTROL" -> throw CustomException("发送频率频繁，请稍后重试")
            "isv.BLACK_KEY_CONTROL_LIMIT" -> throw CustomException("黑名单管控")
            "isv.MOBILE_NUMBER_ILLEGAL" -> throw CustomException("非法手机号")
            "isv.SYSTEM_ERROR" -> throw CustomException("系统错误，请稍后重试")
            "isv.ACCOUNT_ABNORMAL", "isv.AMOUNT_NOT_ENOUGH", "isv.ACCOUNT_NOT_EXISTS" -> throw CustomException("发送失败，请联系管理员")
            else -> throw CustomException("发送失败，请联系管理员")
        }
        RedisUtil.set("${Constant.Redis.SMS_CHECK}$mobile", code, 5 * 60 * 1000)
    }
}
