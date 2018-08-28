package com.basicfu.sip.client.common

import com.basicfu.sip.client.common.redis.SipLogsRedisConfig
import com.basicfu.sip.client.controller.UtilController
import com.basicfu.sip.client.util.DictUtil
import com.basicfu.sip.client.util.LogsUtil
import com.basicfu.sip.client.util.MailUtil
import com.basicfu.sip.client.util.UserUtil

/**
 * @author basicfu
 * @date 2018/7/16
 */
enum class Function(val value: Array<Class<*>>) {
    User(arrayOf(UserUtil::class.java)),
    Dict(arrayOf(DictUtil::class.java)),
    Mail(arrayOf(MailUtil::class.java)),
    Interface(arrayOf(UtilController::class.java)),
    Logs(arrayOf(LogsUtil::class.java,SipLogsRedisConfig::class.java)),
}
