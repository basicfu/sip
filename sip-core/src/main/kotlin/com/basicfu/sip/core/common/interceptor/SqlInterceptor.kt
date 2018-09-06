package com.basicfu.sip.core.common.interceptor

import com.alibaba.druid.sql.SQLUtils
import com.alibaba.druid.sql.ast.SQLExpr
import com.alibaba.druid.sql.ast.expr.*
import com.alibaba.druid.sql.ast.statement.*
import com.basicfu.sip.core.common.Constant
import com.basicfu.sip.core.common.autoconfig.Config
import com.basicfu.sip.core.util.RequestUtil
import com.mysql.jdbc.DatabaseMetaData
import org.apache.commons.lang3.NotImplementedException
import org.apache.commons.lang3.StringUtils
import org.apache.ibatis.executor.statement.StatementHandler
import org.apache.ibatis.plugin.*
import org.apache.ibatis.reflection.SystemMetaObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.util.ReflectionUtils
import java.sql.Connection
import java.util.*


//
//@Intercepts(
//    Signature(type = Executor::class, method = "update", args = arrayOf(MappedStatement::class, Any::class)),
//    Signature(
//        type = Executor::class,
//        method = "query",
//        args = arrayOf(MappedStatement::class, Any::class, RowBounds::class, ResultHandler::class)
//    ),
//    Signature(
//        type = Executor::class,
//        method = "query",
//        args = arrayOf(
//            MappedStatement::class,
//            Any::class,
//            RowBounds::class,
//            ResultHandler::class,
//            CacheKey::class,
//            BoundSql::class
//        )
//    )
//)
/**
 * @author basicfu
 * @date 2018/9/4
 * sql拦截器部分查询可能无法拦截(需加query会拦截两次，待具体测试)
 */
@Component
@Intercepts(
    Signature(type = StatementHandler::class, method = "prepare", args = arrayOf(Connection::class, Integer::class))
//    Signature(type = StatementHandler::class, method = "query", args = arrayOf(Statement::class, ResultHandler::class))
)
class SqlInterceptor : Interceptor {
    val dialect = "MYSQL"
    @Autowired
    lateinit var config: Config

    override fun intercept(invocation: Invocation): Any {
        val metaData = (invocation.args[0] as Connection).metaData
        val field = ReflectionUtils.findField(DatabaseMetaData::class.java, "database")
        field.isAccessible = true
        val databaseName = field.get(metaData).toString()
        val statementHandler = invocation.target as StatementHandler
        val boundSql = statementHandler.boundSql
        @Suppress("UNCHECKED_CAST")
        val metaObject = SystemMetaObject.forObject(statementHandler)
        //name like null 上次过滤NULL问题，
        val appId: String = RequestUtil.getParameter(Constant.System.APP_CODE)
                ?: //log
                throw RuntimeException("not found app code")
        //TODO 应添加当系统设置某个参数或线程时跳过过滤sql
        metaObject.setValue("delegate.boundSql.sql", addCondition(databaseName, boundSql.sql, config.appField, appId))
        return invocation.proceed()
    }

    override fun plugin(target: Any): Any {
        return Plugin.wrap(target, this)
    }

    override fun setProperties(properties: Properties) {
    }

    fun addCondition(databaseName: String, sql: String, field: String, value: String): String {
        val statementList = SQLUtils.parseStatements(sql, dialect)
        if (statementList == null || statementList.size == 0) return sql
        val sqlStatement = statementList[0]
        when (sqlStatement) {
            is SQLSelectStatement -> {
                val queryObject = sqlStatement.select.query as SQLSelectQueryBlock
                addSelectStatementCondition(databaseName, queryObject, queryObject.from, field, value)
            }
            is SQLUpdateStatement -> addUpdateStatementCondition(databaseName, sqlStatement, field, value)
            is SQLDeleteStatement -> addDeleteStatementCondition(databaseName, sqlStatement, field, value)
            is SQLInsertStatement -> addInsertStatementCondition(databaseName, sqlStatement, field, value)
        }
        return SQLUtils.toSQLString(statementList, dialect)
    }

    private fun addSelectStatementCondition(
        databaseName: String,
        queryObject: SQLSelectQueryBlock?,
        from: SQLTableSource?,
        fieldName: String,
        fieldValue: String
    ) {
        if (StringUtils.isBlank(fieldName) || from == null || queryObject == null) return
        val originCondition = queryObject.where
        var dbName=databaseName
        when (from) {
            is SQLExprTableSource -> {
                var tableName = ""
                if (from.expr is SQLIdentifierExpr) {
                    tableName = (from.expr as SQLIdentifierExpr).name
                } else if (from.expr is SQLPropertyExpr) {
                    //此种情况目前只发现在`数据库名.表明`的情况，取owner中的库名
                    val sqlPropertyExpr=from.expr as SQLPropertyExpr
                    dbName=sqlPropertyExpr.ownernName.replace("`","")
                    tableName = sqlPropertyExpr.name
                }
                val alias = from.alias
                val newCondition =
                    newEqualityCondition(dbName, tableName, alias, fieldName, fieldValue, originCondition)
                queryObject.where = newCondition
            }
            is SQLJoinTableSource -> {
                val joinObject = from as SQLJoinTableSource?
                val left = joinObject!!.left
                val right = joinObject.right

                addSelectStatementCondition(dbName, queryObject, left, fieldName, fieldValue)
                addSelectStatementCondition(dbName, queryObject, right, fieldName, fieldValue)

            }
            is SQLSubqueryTableSource -> {
                val subSelectObject = from.select
                val subQueryObject = subSelectObject.query as SQLSelectQueryBlock
                addSelectStatementCondition(dbName, subQueryObject, subQueryObject.from, fieldName, fieldValue)
            }
            else -> throw NotImplementedException("未处理的异常")
        }
    }

    private fun addInsertStatementCondition(
        databaseName: String,
        insertStatement: SQLInsertStatement?,
        fieldName: String,
        fieldValue: String
    ) {
        if (insertStatement != null) {
            val sqlSelect = insertStatement.query
            if (sqlSelect != null) {
                val selectQueryBlock = sqlSelect.query as SQLSelectQueryBlock
                addSelectStatementCondition(
                    databaseName,
                    selectQueryBlock,
                    selectQueryBlock.from,
                    fieldName,
                    fieldValue
                )
            }
        }
    }

    private fun addUpdateStatementCondition(
        databaseName: String,
        updateStatement: SQLUpdateStatement,
        fieldName: String,
        fieldValue: String
    ) {
        val where = updateStatement.where
        //添加子查询中的where条件
        addSQLExprCondition(databaseName, where, fieldName, fieldValue)
        val newCondition = newEqualityCondition(
            databaseName, updateStatement.tableName.simpleName,
            updateStatement.tableSource.alias, fieldName, fieldValue, where
        )
        updateStatement.where = newCondition
    }

    private fun addDeleteStatementCondition(
        databaseName: String,
        deleteStatement: SQLDeleteStatement,
        fieldName: String,
        fieldValue: String
    ) {
        val where = deleteStatement.where
        addSQLExprCondition(databaseName, where, fieldName, fieldValue)
        val newCondition = newEqualityCondition(
            databaseName, deleteStatement.tableName.simpleName,
            deleteStatement.tableSource.alias, fieldName, fieldValue, where
        )
        deleteStatement.where = newCondition

    }

    /**
     * 拼接修改sql
     * 判断是否要过滤应用
     */
    private fun newEqualityCondition(
        databaseName: String,
        tableName: String,
        tableAlias: String?,
        fieldName: String,
        fieldValue: String?,
        originCondition: SQLExpr?
    ): SQLExpr? {
        val executeTable = config.appExecuteTable[databaseName]
        return if (executeTable != null && executeTable.contains(tableName)) {
            originCondition
        } else {
            val filedName = if (StringUtils.isBlank(tableAlias)) fieldName else "$tableAlias.$fieldName"
            val condition =
                SQLBinaryOpExpr(SQLIdentifierExpr(filedName), SQLCharExpr(fieldValue), SQLBinaryOperator.Equality)
            return SQLUtils.buildCondition(SQLBinaryOperator.BooleanAnd, condition, false, originCondition)
        }
    }

    private fun addSQLExprCondition(databaseName: String, where: SQLExpr, fieldName: String, fieldValue: String) {
        when (where) {
            is SQLInSubQueryExpr -> {
                val subSelectObject = where.getSubQuery()
                val subQueryObject = subSelectObject.query as SQLSelectQueryBlock
                addSelectStatementCondition(databaseName, subQueryObject, subQueryObject.from, fieldName, fieldValue)
            }
            is SQLBinaryOpExpr -> {
                val left = where.left
                val right = where.right
                addSQLExprCondition(databaseName, left, fieldName, fieldValue)
                addSQLExprCondition(databaseName, right, fieldName, fieldValue)
            }
            is SQLQueryExpr -> {
                val selectQueryBlock = where.getSubQuery().query as SQLSelectQueryBlock
                addSelectStatementCondition(
                    databaseName,
                    selectQueryBlock,
                    selectQueryBlock.from,
                    fieldName,
                    fieldValue
                )
            }
        }
    }

}
