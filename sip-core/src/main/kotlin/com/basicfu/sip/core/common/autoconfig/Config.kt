package com.basicfu.sip.core.common.autoconfig

import org.springframework.stereotype.Component
import org.yaml.snakeyaml.Yaml
import java.io.FileInputStream
import javax.annotation.PostConstruct

@Component
class Config {
    /**需要排除app_id的表*/
    var appField="app_id"
    var appExecuteTable=HashMap<String,ArrayList<String>>()

    @Suppress("UNCHECKED_CAST")
    @PostConstruct
    fun init() {
        val yaml = Yaml().load(FileInputStream(ClassLoader.getSystemResource("application-app.yaml").file)) as LinkedHashMap<String, Any>
        yaml["sip"]?.let {
            val app=(it as LinkedHashMap<String, Any>)
            appField=app["app-field"].toString()
            app["app-exclude-table"]?.let {
                appExecuteTable=it as LinkedHashMap<String,ArrayList<String>>
            }
        }
    }
}
