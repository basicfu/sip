package com.basicfu.sip.base.util

import com.aliyuncs.CommonRequest
import com.aliyuncs.DefaultAcsClient
import com.aliyuncs.exceptions.ClientException
import com.aliyuncs.exceptions.ServerException
import com.aliyuncs.http.MethodType
import com.aliyuncs.profile.DefaultProfile
import com.basicfu.sip.base.common.constant.Constant
import com.basicfu.sip.core.util.RedisUtil

object SmsUtil {
    fun send(mobile: String) {
        //根据配置文件取值，所以此处实时创建
        val profile: DefaultProfile = DefaultProfile.getProfile("default", "<accessKeyId>", "<accessSecret>")
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
        request.putQueryParameter("TemplateCode", "id")
        request.putQueryParameter("TemplateParam", "{'code':\"$code\"}")
        try {
            val response = client.getCommonResponse(request)
            println(response.data)
//            when(""){
//
//            }
        } catch (e: ServerException) {
            e.printStackTrace()
        } catch (e: ClientException) {
            e.printStackTrace()
        }
        RedisUtil.set("${Constant.Redis.SMS_CHECK}$mobile",code,5*60*1000)
    }
}

// case "isv.MOBILE_NUMBER_ILLEGAL":
//         msg = "手机号码格式错误";
//         break;
//         case "isv.BUSINESS_LIMIT_CONTROL":
//         msg = "获取频繁，请稍后重试";//	短信验证码，使用同一个签名，对同一个手机号码发送短信验证码，支持1条/分钟，5条/小时，10条/天。一个手机号码通过阿里大于平台只能收到40条/天。 短信通知，使用同一签名、同一模板，对同一手机号发送短信通知，允许每天50条（自然日）。
//         break;
//         case "isv.AMOUNT_NOT_ENOUGH":
//         msg = "获取手机号码错误，请联系管理员";//实际错误为账号余额不足
//         break;
//default:
//        msg = "获取手机号码错误，请联系管理员";
//        break;
