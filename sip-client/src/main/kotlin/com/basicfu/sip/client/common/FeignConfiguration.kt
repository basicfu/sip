package com.basicfu.sip.client.common

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
 * @author basicfu
 * @date 2018/7/18
 */
@Component
class FeignConfiguration {
    @Value("\${sip.app:}")
    private val app: String? = null
    @Value("\${sip.secret:}")
    private val secret: String? = null

}
