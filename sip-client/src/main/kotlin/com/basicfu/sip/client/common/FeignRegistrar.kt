package com.basicfu.sip.client.common

import org.springframework.cloud.netflix.feign.EnableFeignClients

/**
 * @author basicfu
 * @date 2018/7/16
 */
@EnableFeignClients(basePackages = ["com.basicfu.sip.client.feign"])
class FeignRegistrar
