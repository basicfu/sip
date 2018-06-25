package com.basicfu.sip.core.annotation

import com.basicfu.sip.core.BeanRegistrar
import org.springframework.context.annotation.Import
import java.lang.annotation.Documented
import java.lang.annotation.Inherited
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Target(AnnotationTarget.CLASS)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(BeanRegistrar::class)
annotation class EnableRedis
