package com.basicfu.sip.client.common.redis

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.stereotype.Component
import redis.clients.jedis.JedisPoolConfig

/**
 * @author basicfu
 * @date 2018/8/24
 */
@Component
class SipRedisConfig {

    @Value("\${sip.redis.host:}")
    private val host: String? = null
    @Value("\${sip.redis.port:}")
    private val port: Int = 0
    @Value("\${sip.redis.password:}")
    private val password: String? = null

    @Bean
    fun sipRedisConnectionFactory(): RedisConnectionFactory {
        val poolConfig = JedisPoolConfig()
        val jedisConnectionFactory = JedisConnectionFactory(poolConfig)
        jedisConnectionFactory.database = 0
        jedisConnectionFactory.port = port
        jedisConnectionFactory.hostName = host
        if (!password.isNullOrBlank()) {
            jedisConnectionFactory.password = password
        }
        return jedisConnectionFactory
    }

    @Bean(name = ["sipClientRedisTemplate"])
    fun redisTemplate(sipRedisConnectionFactory:RedisConnectionFactory): RedisTemplate<*, *> {
        val redisTemplate = RedisTemplate<Any, Any>()
        redisTemplate.connectionFactory = sipRedisConnectionFactory
        val stringRedisSerializer = StringRedisSerializer()
        redisTemplate.keySerializer = stringRedisSerializer
        redisTemplate.hashKeySerializer = stringRedisSerializer
        redisTemplate.afterPropertiesSet()
        return redisTemplate
    }
}
