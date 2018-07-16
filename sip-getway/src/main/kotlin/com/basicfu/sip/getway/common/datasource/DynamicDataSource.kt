package com.basicfu.sip.getway.common.datasource

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource

class DynamicDataSource : AbstractRoutingDataSource() {

    override fun determineCurrentLookupKey(): Any {
        return DataSourceContextHolder.get()
    }

}
