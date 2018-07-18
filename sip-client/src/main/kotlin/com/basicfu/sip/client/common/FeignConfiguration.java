package com.basicfu.sip.client.common;

import com.basicfu.sip.client.util.RequestUtil;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author basicfu
 * @date 2018/7/18
 */
@Configuration
public class FeignConfiguration {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            String header = RequestUtil.INSTANCE.getHeader(Constant.AUTHORIZATION);
            requestTemplate.header(Constant.AUTHORIZATION, header);
        };
    }
}
