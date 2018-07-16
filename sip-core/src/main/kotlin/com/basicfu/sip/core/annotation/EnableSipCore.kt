package com.basicfu.sip.core.annotation

import com.basicfu.sip.core.common.BeanRegistrar
import com.basicfu.sip.core.common.Function
import org.springframework.context.annotation.Import

import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Suppress("DEPRECATED_JAVA_ANNOTATION")
@Retention(RetentionPolicy.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Documented
@Import(BeanRegistrar::class)
annotation class EnableSipCore(val enable: Array<Function> = [], val disable: Array<Function> = [])
