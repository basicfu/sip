package com.basicfu.sip.client.annotation

import com.basicfu.sip.client.common.BeanRegistrar
import com.basicfu.sip.client.common.FilterRegistrar
import org.springframework.context.annotation.Import
import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Suppress("DEPRECATED_JAVA_ANNOTATION")
@Retention(RetentionPolicy.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Documented
@Import(BeanRegistrar::class, FilterRegistrar::class)
//@EnableHystrix
annotation class EnableSipClient
