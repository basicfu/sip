package com.basicfu.sip.core.util

@Suppress("UNCHECKED_CAST")
object ThreadLocalUtil {
    private val threadLocal = object : ThreadLocal<HashMap<String, Any>>() {
        override fun initialValue(): HashMap<String, Any> {
            return HashMap()
        }
    }

    operator fun <T> get(key: String): T {
        println(Thread.currentThread().name)
        return threadLocal.get()[key] as T
    }

    operator fun set(key: String, value: Any) {
        println(Thread.currentThread().name)
        threadLocal.get()[key] = value
    }

    fun remove(key: String) {
        threadLocal.get().remove(key)
    }

    fun clear() {
        threadLocal.remove()
    }
}
