package com.basicfu.sip.getway.common.datasource

class DataSourceContextHolder {
    enum class DataSourceType {
        BASE,
        PERMISSION,
        DICT
    }
    companion object {
        private val contextHolder = ThreadLocal<DataSourceType>()
        fun get(): String {
            return contextHolder.get().name
        }
        fun base() {
            contextHolder.set(DataSourceType.BASE)
        }
        fun permission() {
            contextHolder.set(DataSourceType.PERMISSION)
        }
        fun dict() {
            contextHolder.set(DataSourceType.DICT)
        }
    }

}
