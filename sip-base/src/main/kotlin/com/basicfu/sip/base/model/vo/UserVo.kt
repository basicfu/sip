package com.basicfu.sip.base.model.vo

import com.alibaba.fastjson.JSONObject

class UserVo {
    var id: String? = null
    //提供用户名(默认)、手机号(需验证)、邮箱注册(需验证)
    var username: String? = null
    var nickname: String? = null
    var mobile:String?=null
    var email:String?=null
    var password: String? = null
    //json字段
    var content=JSONObject()
    var cdate: Int? = null
    var udate: Int? = null
    var type: String? = null
    var status: Int? = null

    var ids:List<Long>?=null
    var roleIds:List<Long>?=null
    /**
     * 源密码
     */
    var orignPassword: String? = null
    /**
     * 密码是否加密
     */
    var encry:Boolean=true

    var q:String?=null
    /**验证码*/
    var code:String?=null
}
