package com.basicfu.sip.core.mapper

import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.SelectProvider
import org.apache.ibatis.annotations.UpdateProvider

interface CommonMapper<T> {

    @SelectProvider(type = CommonProvider::class, method = "dynamicSQL")
    fun selectCountBySql(@Param("sql") sql: String): Int?

    @SelectProvider(type = CommonProvider::class, method = "dynamicSQL")
    fun selectBySql(@Param("sql") sql: String): List<T>

    @UpdateProvider(type = CommonProvider::class, method = "dynamicSQL")
    fun updateBySql(@Param("sql") sql: String): Int
}
