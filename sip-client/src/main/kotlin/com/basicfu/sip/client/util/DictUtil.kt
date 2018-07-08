package com.basicfu.sip.client.util

import com.basicfu.sip.client.feign.DictFeign
import com.basicfu.sip.client.model.DictDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class DictUtil {
    @Autowired
    private lateinit var dictFeignTmp: DictFeign

    @PostConstruct
    fun init() {
        DictUtil.dictFeign = dictFeignTmp
    }

    companion object {
        private lateinit var dictFeign: DictFeign

        /**
         * 根据字典value获取字典
         */
        fun get(value: String): List<DictDto>? {
            return dictFeign.get(value).data
        }

        /**
         * 根据字典value获取字典(返回Map<Value,DictDto>)
         */
        fun getMap(value: String): Map<String, DictDto> {
            return get(value)?.associateBy({ it.value!! }, { it })?:HashMap()
        }
    }
}
