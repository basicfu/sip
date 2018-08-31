package com.basicfu.sip.core.common.mapper

import org.apache.ibatis.mapping.MappedStatement
import tk.mybatis.mapper.mapperhelper.MapperHelper
import tk.mybatis.mapper.mapperhelper.MapperTemplate
import tk.mybatis.mapper.mapperhelper.SqlHelper

@Suppress("UNUSED_PARAMETER")
class CommonProvider(mapperClass: Class<*>, mapperHelper: MapperHelper) : MapperTemplate(mapperClass, mapperHelper) {

    fun selectCountBySql(ms: MappedStatement): String {
        return "\${sql}"
    }

    fun selectBySql(ms: MappedStatement): String {
        return "\${sql}"
    }
    fun selectLastInsertId(ms: MappedStatement): String {
        return "SELECT LAST_INSERT_ID()"
    }
    fun updateBySql(ms: MappedStatement): String {
        val entityClass = getEntityClass(ms)
        val sql = StringBuilder()
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)))
        sql.append("\${sql}")
        return sql.toString()
    }
}
