package com.basicfu.sip.client.common

import com.basicfu.sip.client.util.RequestUtil
import feign.RequestInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author basicfu
 * @date 2018/7/18
 */
@Configuration
class FeignConfiguration {
    @Bean
    fun requestInterceptor(): RequestInterceptor {
        return RequestInterceptor { template ->
            val header = RequestUtil.getHeader(Constant.AUTHORIZATION)
            template.header(Constant.AUTHORIZATION, header)
        }
    }
}
