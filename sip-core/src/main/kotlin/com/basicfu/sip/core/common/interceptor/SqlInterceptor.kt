package com.basicfu.sip.core.common.interceptor

import org.apache.ibatis.cache.CacheKey
import org.apache.ibatis.executor.Executor
import org.apache.ibatis.mapping.BoundSql
import org.apache.ibatis.mapping.MappedStatement
import org.apache.ibatis.mapping.SqlSource
import org.apache.ibatis.plugin.*
import org.apache.ibatis.session.ResultHandler
import org.apache.ibatis.session.RowBounds
import java.util.*


@Intercepts(
    Signature(type = Executor::class, method = "update", args = arrayOf(MappedStatement::class, Any::class)),
    Signature(
        type = Executor::class,
        method = "query",
        args = arrayOf(MappedStatement::class, Any::class, RowBounds::class, ResultHandler::class)
    ),
    Signature(
        type = Executor::class,
        method = "query",
        args = arrayOf(
            MappedStatement::class,
            Any::class,
            RowBounds::class,
            ResultHandler::class,
            CacheKey::class,
            BoundSql::class
        )
    )
)
class SqlInterceptor : Interceptor {

    override fun intercept(invocation: Invocation): Any {
        val queryArgs = invocation.args
        val mappedStatement = queryArgs[0] as MappedStatement
        val parameter = queryArgs[1]
        val boundSql: BoundSql
        boundSql = if (queryArgs.size == 6) {
            queryArgs[5] as BoundSql
        } else {
            mappedStatement.getBoundSql(parameter)
        }
        val sql = boundSql.sql
//select * from a where appId=1
//select * from a where aaa=1 and appId=1
//select * from a where aaa=1 and appId=1
//        sql = "select * from service"
        val newBoundSql =
            BoundSql(mappedStatement.configuration, sql, boundSql.parameterMappings, boundSql.parameterObject)
        val newMs = copyFromMappedStatement(mappedStatement, BoundSqlSqlSource(newBoundSql))
        for (mapping in boundSql.parameterMappings) {
            val prop = mapping.property
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop))
            }
        }
        queryArgs[0] = newMs
        if (queryArgs.size == 6) {
            queryArgs[5] = newBoundSql
        }
        return invocation.proceed()
    }

    override fun plugin(target: Any): Any {
        return Plugin.wrap(target, this)
    }

    override fun setProperties(properties: Properties) {
        val dialect = properties.getProperty("dialect")
        println("dialect=$dialect")
    }

    private fun copyFromMappedStatement(ms: MappedStatement, newSqlSource: SqlSource): MappedStatement {
        val builder = MappedStatement.Builder(ms.configuration, ms.id, newSqlSource, ms.sqlCommandType)
        builder.resource(ms.resource)
        builder.fetchSize(ms.fetchSize)
        builder.statementType(ms.statementType)
        builder.keyGenerator(ms.keyGenerator)
        if (ms.keyProperties != null && ms.keyProperties.isNotEmpty()) {
            builder.keyProperty(ms.keyProperties[0])
        }
        builder.timeout(ms.timeout)
        builder.parameterMap(ms.parameterMap)
        builder.resultMaps(ms.resultMaps)
        builder.resultSetType(ms.resultSetType)
        builder.cache(ms.cache)
        builder.flushCacheRequired(ms.isFlushCacheRequired)
        builder.useCache(ms.isUseCache)
        return builder.build()
    }

    class BoundSqlSqlSource(private val boundSql: BoundSql) : SqlSource {
        override fun getBoundSql(parameterObject: Any): BoundSql {
            return boundSql
        }
    }
}
