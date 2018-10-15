package com.basicfu.sip.client.common

import com.basicfu.sip.client.util.RequestUtil
import feign.RequestInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.net.URLEncoder

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
            // app通过url方式转发，已做编码处理
            template.query(true,Constant.APP_CODE, URLEncoder.encode(RequestUtil.getParameter(Constant.APP_CODE),Charsets.UTF_8.name()))
        }
    }
}
