package com.basicfu.sip.core.common.datasource

class DataSourceContextHolder {
    enum class DataSourceType {
        MASTER,
        READ
    }

    fun write() {
        contextHolder.set(DataSourceType.MASTER)
    }

    fun get(): String {
        return contextHolder.get().name
    }

    companion object {

        private val contextHolder = ThreadLocal<DataSourceType>()

        fun read() {
            contextHolder.set(DataSourceType.READ)
        }
    }

}
