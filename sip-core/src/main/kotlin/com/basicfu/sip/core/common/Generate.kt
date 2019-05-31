package com.basicfu.sip.core.common

inline fun <reified T> generate(op: T.() -> Unit = {}): T {
    val instance = T::class.java.newInstance()
    op(instance)
    return instance
}
