package com.basicfu.sip.core.datasource

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource

import java.util.concurrent.atomic.AtomicLong

class DynamicDataSource(private val writeDataSourceNumber: Int, private val readDataSourceNumber: Int) :
    AbstractRoutingDataSource() {
    private val readCount: AtomicLong = AtomicLong(0L)
    private val writeCount: AtomicLong = AtomicLong(0L)

    override fun determineCurrentLookupKey(): Any {
        return (DataSourceContextHolder.DataSourceType.MASTER.name + ""
                + this.writeCount.getAndAdd(1L) % this.writeDataSourceNumber.toLong())
    }

}
