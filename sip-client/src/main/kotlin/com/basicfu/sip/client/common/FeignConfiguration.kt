package com.basicfu.sip.client.common

import com.basicfu.sip.client.model.AppDto
import com.basicfu.sip.client.util.RedisUtil
import com.basicfu.sip.client.util.RequestUtil
import feign.RequestInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.net.URI

/**
 * @author basicfu
 * @date 2018/7/18
 */
@Component
class FeignConfiguration {
    @Value("\${sip.app:}")
    private val app: String? = null
//    @Value("\${sip.secret:}")
//    private val secret: String? = null
//    @Value("\${sip.call:}")
//    private val call: String? = null

    @Bean
    fun requestInterceptor(): RequestInterceptor {
        return RequestInterceptor { template ->
            //如果是获取所有资源接口，特殊处理
            val path = URI(template.url()).path
            if (path == "/sip/client/url") {
                val service = (template.queries()["service"] as ArrayList)[0]
                if (service.startsWith("http://")) {
                    template.insert(0, service)
                } else {
                    template.insert(0, "http://$service")
                }
            }
            //TODO 不走getway时需要是json格式处理，可考虑在core中过滤器实现如果只有code再次处理转为json格式
            template.header(Constant.AUTHORIZATION, RequestUtil.getHeader(Constant.AUTHORIZATION))
            if (app.isNullOrBlank()) {
                // feign需要转发app通过url方式
                template.query(Constant.GETWAY_APP_ID, RequestUtil.getParameter(Constant.GETWAY_APP_ID))
                template.query(Constant.GETWAY_APP_CODE, RequestUtil.getParameter(Constant.GETWAY_APP_CODE))
            } else {
                val app =
                    RedisUtil.hGet<AppDto>(Constant.Redis.APP, app) ?: throw RuntimeException("not found app code")
                template.query(Constant.GETWAY_APP_ID, app.id.toString())
                template.query(Constant.GETWAY_APP_CODE, app.code)
            }
        }
    }
}
