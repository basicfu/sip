package com.basicfu.sip.client.util

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import jdk.nashorn.internal.runtime.regexp.joni.Config.log
import org.apache.coyote.http11.Constants.a
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.connection.RedisConnection
import org.springframework.data.redis.core.RedisCallback
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.text.SimpleDateFormat
import javax.annotation.PostConstruct
import javax.annotation.Resource

@Suppress("UNCHECKED_CAST")
@Component
class LogsUtil {

    @Value("\${sip.logs.local:false}")
    private  val enableLocalTmp: Boolean = false

    @Autowired
    @Resource(name = "sipLogsRedisTemplate")
    private lateinit var redisTemplateTmp: RedisTemplate<Any, Any>

    @PostConstruct
    fun init() {
        redisTemplate = redisTemplateTmp
        enableLocal = enableLocalTmp
    }

    companion object {
        internal lateinit var redisTemplate: RedisTemplate<Any, Any>
        internal var enableLocal =false
        private val simpleDateFormat=SimpleDateFormat("yyyy-MM-dd")
        private val log = LoggerFactory.getLogger(this::class.java)

        private fun set(msg: Any){
            val json = JSON.parseObject(JSON.toJSONString(msg))
            if(!json.containsKey("templateCode")){
                throw RuntimeException("msg must contains templateCode")
            }
            if(json.size<2){
                throw RuntimeException("msg must contains a valid value")
            }
            val currentTimeMillis = System.currentTimeMillis()
            json["logsTime"]=currentTimeMillis

//            redisTemplate.opsForList().rightPush()
        }
        private fun local(){
            if(enableLocal){
//                log.info("$templateCode-$msg")
            }
        }

        fun info(msg:Any){
            set(msg)
        }
    }
}

//暂用本地时间
//val a= LogsUtil.redisTemplate.execute {
//        connection -> connection.time()
//}
