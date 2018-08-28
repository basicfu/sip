package com.basicfu.sip.core.common

import com.basicfu.sip.core.common.datasource.DataSourceConfig
import com.basicfu.sip.core.common.handler.GlobalExceptionHandler
import com.basicfu.sip.core.common.redis.RedisConfig
import com.basicfu.sip.core.util.RedisUtil

/**
 * @author basicfu
 * @date 2018/7/16
 */
enum class Function(val value: Array<Class<*>>) {
    Redis(arrayOf(RedisUtil::class.java,RedisConfig::class.java)),
    DataSource(arrayOf(DataSourceConfig::class.java)),
    GlobalException(arrayOf(GlobalExceptionHandler::class.java))
}
