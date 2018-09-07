package com.basicfu.sip.client.common

import com.basicfu.sip.client.util.RequestUtil
import feign.RequestInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

/**
 * @author basicfu
 * @date 2018/7/18
 */
@Component
class FeignConfiguration {
    @Bean
    fun requestInterceptor(): RequestInterceptor {
        return RequestInterceptor { template ->
            template.header(Constant.AUTHORIZATION, RequestUtil.getHeader(Constant.AUTHORIZATION))
            template.query(Constant.APP_CODE, RequestUtil.getParameter(Constant.APP_CODE))
        }
    }
}
