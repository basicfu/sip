package com.basicfu.sip.client.annotation

import com.basicfu.sip.client.common.BeanRegistrar
import com.basicfu.sip.client.common.FeignRegistrar
import com.basicfu.sip.client.common.Function
import org.springframework.cloud.netflix.feign.EnableFeignClients
import org.springframework.context.annotation.Import
import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Suppress("DEPRECATED_JAVA_ANNOTATION")
@Retention(RetentionPolicy.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Documented
@Import(BeanRegistrar::class, FeignRegistrar::class)
@EnableFeignClients
annotation class EnableSipClient(val enable: Array<Function> = [], val disable: Array<Function> = [])
