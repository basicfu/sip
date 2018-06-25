package com.basicfu.sip.core.util

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct


@Component
class RedisUtil {
    @Autowired
    private lateinit var redisTemplateTmp: RedisTemplate<Any, Any>

    @PostConstruct
    fun init() {
        redisTemplate = redisTemplateTmp
    }

    companion object {
        private val timeUnit = TimeUnit.MILLISECONDS
        lateinit var redisTemplate: RedisTemplate<Any, Any>

        fun set(k: Any, v: Any?) {
            val serialize = SerializationUtil.serialize(v)
            redisTemplate.opsForValue().set(k, serialize)
        }

        inline fun <reified T> get(k: Any): T? {
            val data = redisTemplate.opsForValue().get(k)
            return SerializationUtil.deserialize(data)
        }

        fun expire(key: Any, expireTime: Long) {
            redisTemplate.expire(key, expireTime, timeUnit)
        }

        fun del(key: Any) {
            redisTemplate.delete(key)
        }

        fun hSet(key: Any, hk: Any, hv: Any?) {
            val serialize = SerializationUtil.serialize(hv)
            redisTemplate.opsForHash<Any, Any>().put(key, hk, serialize)
        }

        inline fun <reified T> hGet(key: Any, hk: Any): T? {
            val data = redisTemplate.opsForHash<Any, Any>().get(key.toString(), hk.toString())
            return SerializationUtil.deserialize(data as ByteArray)

        }

        inline fun <reified T : Any> hMSet(key: String, map: Map<String, T?>) {
            val result = hashMapOf<String, ByteArray?>()
            map.forEach { k, v ->
                result[k] = SerializationUtil.serialize(v)
            }
            redisTemplate.opsForHash<Any, Any>().putAll(key, result)
        }

        inline fun <reified T> hGetAll(key: Any): HashMap<String, T?> {
            val entries = redisTemplate.opsForHash<String, T>().entries(key)
            val result = hashMapOf<String, T?>()
            entries.forEach { k, v ->
                result[k] = SerializationUtil.deserialize(v)
            }
            return result
        }

        fun hDel(key: Any, hk: Any) {
            redisTemplate.opsForHash<Any, Any>().delete(key, hk)
        }

        fun exists(key: Any): Boolean {
            return redisTemplate.hasKey(key)
        }

        fun multi() {
            redisTemplate.multi()
        }

        fun exec() {
            redisTemplate.exec()
        }

        fun increment(key: Any, l: Long): Long {
            return redisTemplate.opsForValue().increment(key, l)
        }

        fun sadd(key: Any, vararg objs: Any): Long {
            return redisTemplate.opsForSet().add(key, *objs)
        }

        fun scard(key: Any): Long {
            return redisTemplate.boundSetOps(key).size()
        }

    }
}
