package com.basicfu.sip.base.common.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class AliyunConfig {
    @Value("\${aliyun.accessId:}")
    var accessId: String = ""
    @Value("\${aliyun.accessKey:}")
    var accessKey: String = ""

    @PostConstruct
    fun init() {
        AccessId = accessId
        AccessKey = accessKey
    }

    companion object {
        var AccessId: String = ""
        var AccessKey: String = ""
    }
}
