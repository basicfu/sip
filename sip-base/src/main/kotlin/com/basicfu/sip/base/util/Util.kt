package com.basicfu.sip.base.util

import org.springframework.beans.BeanUtils

/**
 * bean copy
 * obj源对象,R目标对象
 */
inline fun <reified R> copy(obj: Any?): R? {
    if (obj == null) {
        return null
    }
    val clazz = R::class.java
    val instance = clazz.newInstance()!!
    BeanUtils.copyProperties(obj, instance)
    return instance
}

/**
 * list<bean> copy
 * obj源对象,R目标对象
 * 如果T和R是同一个类型是否能优化省性能
 */
inline fun <reified R> copy(list: List<Any>): MutableList<R> {
    val clazz = R::class.java
    val result = arrayListOf<R>()
    list.forEach {
        val instance = clazz.newInstance()!!
        BeanUtils.copyProperties(it, instance)
        result.add(instance)
    }
    return result
}
