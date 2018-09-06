package com.basicfu.sip.core.common.autoconfig

import org.springframework.core.io.support.PropertiesLoaderUtils
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class Config {
    /**需要排除app_id的表*/
    var appField = "app_id"
    var appExecuteTable = HashMap<String, List<String>>()

    @Suppress("UNCHECKED_CAST")
    @PostConstruct
    fun init() {
        val properties = PropertiesLoaderUtils.loadAllProperties("application.properties")
        appField = properties.getProperty("sip.app-field")
        properties.forEach { k, v ->
            val key = k.toString()
            if (key.startsWith("sip.app-exclude-table")) {
                val array = key.split(".")
                if (array.size == 3) {
                    appExecuteTable[array[2]] = v.toString().split(",")
                }
            }
        }
    }
}
