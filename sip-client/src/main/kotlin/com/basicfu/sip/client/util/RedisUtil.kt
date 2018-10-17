package com.basicfu.sip.client.util

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct
import javax.annotation.Resource


@Component
class RedisUtil {
    @Autowired
    @Resource(name = "sipClientRedisTemplate")
    private lateinit var redisTemplateTmp: RedisTemplate<String, Any>

    @PostConstruct
    fun init() {
        redisTemplate = redisTemplateTmp
    }

    companion object {
        private val timeUnit = TimeUnit.MILLISECONDS
        lateinit var redisTemplate: RedisTemplate<String, Any>

        fun set(k: String, v: Any?) {
            val serialize = SerializationUtil.serialize(v)
            redisTemplate.opsForValue().set(k, serialize)
        }

        fun set(k: String, v: Any?, expireMilliseconds: Long) {
            val serialize = SerializationUtil.serialize(v)
            redisTemplate.opsForValue().set(k, serialize)
            expire(k, expireMilliseconds)
        }

        inline fun <reified T> get(k: Any): T? {
            val data = redisTemplate.opsForValue().get(k)
            return SerializationUtil.deserialize(data)
        }

        fun keys(pattern: String):Set<String> {
            return redisTemplate.keys(pattern)
        }

        fun expire(key: String, expireMilliseconds: Long) {
            redisTemplate.expire(key, expireMilliseconds, timeUnit)
        }

        fun del(key: String) {
            redisTemplate.delete(key)
        }

        fun del(keys:List<String>){
            redisTemplate.delete(keys)
        }

        fun hSet(key: String, hk: Any, hv: Any?) {
            val serialize = SerializationUtil.serialize(hv)
            redisTemplate.opsForHash<Any, Any>().put(key, hk, serialize)
        }

        inline fun <reified T> hGet(key: String, hk: Any): T? {
            val data = redisTemplate.opsForHash<Any, Any>().get(key, hk)
            return SerializationUtil.deserialize(data)
        }

        inline fun <reified T : Any> hMSet(key: String, map: Map<String, T?>) {
            val result = hashMapOf<String, Any?>()
            map.forEach { k, v ->
                result[k] = SerializationUtil.serialize(v)
            }
            redisTemplate.opsForHash<Any, Any>().putAll(key, result)
        }

        inline fun <reified T> hGetAll(key: String): HashMap<String, T?> {
            val entries = redisTemplate.opsForHash<String, T>().entries(key)
            val result = hashMapOf<String, T?>()
            entries.forEach { k, v ->
                result[k] = SerializationUtil.deserialize(v)
            }
            return result
        }

        fun hDel(key: String, hk: Any) {
            redisTemplate.opsForHash<Any, Any>().delete(key, hk)
        }

        fun exists(key: String): Boolean {
            return redisTemplate.hasKey(key)
        }

        fun multi() {
            redisTemplate.multi()
        }

        fun exec() {
            redisTemplate.exec()
        }

        fun increment(key: String, l: Long): Long {
            return redisTemplate.opsForValue().increment(key, l)
        }

        fun sadd(key: String, vararg objs: Any): Long {
            return redisTemplate.opsForSet().add(key, *objs)
        }

        fun scard(key: String): Long {
            return redisTemplate.boundSetOps(key).size()
        }

        fun rpush(k: String, v: Any?) {
            val serialize = SerializationUtil.serialize(v)
            redisTemplate.opsForList().rightPush(k, serialize)
        }
    }
}
