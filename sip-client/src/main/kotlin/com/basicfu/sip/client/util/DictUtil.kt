package com.basicfu.sip.client.util

import com.basicfu.sip.client.feign.BaseFeign
import com.basicfu.sip.client.model.DictDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class DictUtil {
    @Autowired
    private lateinit var baseFeignTmp: BaseFeign

    @PostConstruct
    fun init() {
        DictUtil.baseFeign = baseFeignTmp
    }

    companion object {
        private lateinit var baseFeign: BaseFeign

        /**
         * 根据字典value获取字典
         */
        fun get(value: String): List<DictDto>? {
            return baseFeign.get(value).data
        }

        /**
         * 根据字典value获取字典(返回Map<Value,DictDto>)
         */
        fun getMap(value: String): Map<String, DictDto> {
            return get(value)?.associateBy({ it.value!! }, { it }) ?: HashMap()
        }
    }
}
