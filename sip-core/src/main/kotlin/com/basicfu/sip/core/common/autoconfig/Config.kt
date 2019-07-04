package com.basicfu.sip.core.common.autoconfig

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class Config {
    @Value("\${sip.multi-app.enabled:false}")
    var enabled = false
    /**需要排除app_id的表*/
    @Value("\${sip.multi-app.field:app_id}")
    var appField = ""
    @Value("\${sip.multi-app.exclude-table:}")
    var appExecuteTable = ArrayList<String>()
    @Value("\${sip.datasource.expression:}")
    var expression = ""
}
