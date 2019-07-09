package com.basicfu.sip.base.common.validator

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention
@Constraint(validatedBy = [(DictValidator::class)])
annotation class Dict(
    val message: String = "dictionary can't be null",
    val dict: String,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
