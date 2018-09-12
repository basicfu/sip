package com.basicfu.sip.core.annotation.validator

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention
@Constraint(validatedBy = [(DictValidator::class)])
annotation class Dict(
    val message: String = "the value of the request is not in the dictionary",
    val dict: String,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
