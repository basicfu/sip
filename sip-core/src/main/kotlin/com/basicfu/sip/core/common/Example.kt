@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.basicfu.sip.core.common

import com.basicfu.sip.core.util.SqlUtil.dealLikeValue
import org.apache.commons.lang3.StringUtils
import org.springframework.util.ReflectionUtils
import tk.mybatis.mapper.MapperException
import tk.mybatis.mapper.mapperhelper.EntityHelper
import tk.mybatis.mapper.util.StringUtil
import kotlin.reflect.KMutableProperty1

inline fun <reified T> example(op: Example<T>.() -> Unit = {}): tk.mybatis.mapper.entity.Example {
    val clazz = T::class.java
    val builder = Example<T>(clazz)
    op(builder)
    return builder.build()
}

open class Sqls<T> {
    val criteria: Criteria = Criteria()

    //根据反射过滤值为NULL的
    @PublishedApi
    internal inline fun <reified T> getValues(op: T.() -> Unit = {}): LinkedHashMap<String, Any> {
        val clazz = T::class.java
        val instance = clazz.newInstance()
        op(instance)
        val fields = T::class.java.declaredFields
        val linkedMapOf = linkedMapOf<String, Any>()
        for (field in fields) {
            field.isAccessible = true
            val value = field.get(instance)
            if (value != null) {
                linkedMapOf[field.name] = value
            }
        }
        return linkedMapOf
    }

    //自定义条件
    fun andCondition(k: String, condition: String) {
        this.criteria.criterions.add(Criterion(k, null, condition, true, true))
    }

    fun orCondition(k: String, condition: String) {
        this.criteria.criterions.add(Criterion(k, null, condition, false, true))
    }

    fun Sqls<T>.andIsNull(vararg k: KMutableProperty1<T, String?>) {
        k.forEach {
            this.andIsNull(it.name)
        }
    }

    private fun andIsNull(k: String) {
        this.criteria.criterions.add(Criterion(k, null, "is null", true, true))
    }

    fun Sqls<T>.andIsNotNull(vararg k: KMutableProperty1<T, String?>) {
        k.forEach {
            this.andIsNotNull(it.name)
        }
    }

    private fun andIsNotNull(k: String) {
        this.criteria.criterions.add(Criterion(k, null, "is not null", true, true))
    }

    fun Sqls<T>.andEqualTo(k: KMutableProperty1<T, *>, v: Any?) {
        this.andEqualTo(k.name, v)
    }

    inline fun <reified T> Sqls<T>.andEqualTo(op: T.() -> Unit = {}) {
        for ((k, v) in this.getValues(op)) {
            this.andEqualTo(k, v)
        }
    }

    @PublishedApi
    internal fun andEqualTo(k: String, v: Any?) {
        this.criteria.criterions.add(Criterion(k, v, "=", true))
    }


    fun Sqls<T>.andNotEqualTo(k: KMutableProperty1<T, *>, v: String) {
        this.andNotEqualTo(k.name, v)
    }

    inline fun <reified T> Sqls<T>.andNotEqualTo(op: T.() -> Unit = {}) {
        for ((k, v) in this.getValues(op)) {
            this.andNotEqualTo(k, v)
        }
    }

    @PublishedApi
    internal fun andNotEqualTo(k: String, v: Any) {
        this.criteria.criterions.add(Criterion(k, v, "<>", true))
    }


    fun Sqls<T>.andGreaterThan(k: KMutableProperty1<T, *>, v: String) {
        this.andGreaterThan(k.name, v)
    }

    inline fun <reified T> Sqls<T>.andGreaterThan(op: T.() -> Unit = {}) {
        for ((k, v) in this.getValues(op)) {
            this.andGreaterThan(k, v)
        }
    }

    @PublishedApi
    internal fun andGreaterThan(k: String, v: Any) {
        this.criteria.criterions.add(Criterion(k, v, ">", true))
    }


    fun Sqls<T>.andGreaterThanOrEqualTo(k: KMutableProperty1<T, *>, v: String) {
        this.andGreaterThanOrEqualTo(k.name, v)
    }

    inline fun <reified T> Sqls<T>.andGreaterThanOrEqualTo(op: T.() -> Unit = {}) {
        for ((k, v) in this.getValues(op)) {
            this.andGreaterThanOrEqualTo(k, v)
        }
    }

    @PublishedApi
    internal fun andGreaterThanOrEqualTo(k: String, v: Any) {
        this.criteria.criterions.add(Criterion(k, v, ">=", true))
    }


    fun Sqls<T>.andLessThan(k: KMutableProperty1<T, *>, v: String) {
        this.andLessThan(k.name, v)
    }

    inline fun <reified T> Sqls<T>.andLessThan(op: T.() -> Unit = {}) {
        for ((k, v) in this.getValues(op)) {
            this.andLessThan(k, v)
        }
    }

    @PublishedApi
    internal fun andLessThan(k: String, v: Any) {
        this.criteria.criterions.add(Criterion(k, v, "<", true))

    }

    fun Sqls<T>.andLessThanOrEqualTo(k: KMutableProperty1<T, *>, v: String) {
        this.andLessThanOrEqualTo(k.name, v)
    }

    inline fun <reified T> Sqls<T>.andLessThanOrEqualTo(op: T.() -> Unit = {}) {
        for ((k, v) in this.getValues(op)) {
            this.andLessThanOrEqualTo(k, v)
        }
    }

    @PublishedApi
    internal fun andLessThanOrEqualTo(k: String, value: Any) {
        this.criteria.criterions.add(Criterion(k, value, "<=", true))
    }

    fun Sqls<T>.andIn(k: KMutableProperty1<T, *>, v: Iterable<*>) {
        this.andIn(k.name, v)
    }

    @PublishedApi
    internal fun andIn(k: String, values: Iterable<*>) {
        val notNullValues = values.filterNotNull()
        if (notNullValues.isEmpty()) {
            throw RuntimeException("list中没有非null元素")
        }
        this.criteria.criterions.add(Criterion(k, notNullValues, "in", true))
    }


    fun Sqls<T>.andNotIn(k: KMutableProperty1<T, *>, v: Iterable<*>) {
        this.andNotIn(k.name, v)
    }

    @PublishedApi
    internal fun andNotIn(k: String, values: Iterable<*>) {
        this.criteria.criterions.add(Criterion(k, values, "not in", true))
    }

    fun Sqls<T>.andBetween(k: KMutableProperty1<T, *>, v1: Any?, v2: Any?) {
        if (v1 == null && v2 != null) {
            this.andLessThanOrEqualTo(k.name, v2)
        } else if (v1 != null && v2 == null) {
            this.andGreaterThanOrEqualTo(k.name, v1)
        } else if (v1 != null && v2 != null) {
            this.andBetween(k.name, v1, v2)
        }
    }

    //between转换为自定义拼接条件
    @PublishedApi
    internal fun andBetween(k: String, v1: Any, v2: Any) {
        this.criteria.criterions.add(
            Criterion(
                k,
                null,
                "between $v1 and $v2",
                true,
                true
            )
        )

    }

    fun Sqls<T>.andNotBetween(k: KMutableProperty1<T, *>, v1: Any, v2: Any) {
        this.andNotBetween(k.name, v1, v2)
    }

    @PublishedApi
    internal fun andNotBetween(k: String, v1: Any, v2: Any) {
        this.criteria.criterions.add(
            Criterion(
                k,
                null,
                "not between $v1 and $v2",
                true,
                true
            )
        )
    }

    fun Sqls<T>.andLike(k: KMutableProperty1<T, *>, v: String?) {
        this.andLike(k.name, dealLikeValue(v))
    }

    inline fun <reified T> Sqls<T>.andLike(op: T.() -> Unit = {}) {
        for ((k, v) in this.getValues(op)) {
            this.andLike(k, dealLikeValue(v.toString()))
        }
    }

    //手动拼写条件，否则无法使用escape
    //此处的v是condition可以sql注入,like中强制忽略为null的
    @PublishedApi
    internal fun andLike(k: String, v: Any?) {
        v?.let { this.criteria.criterions.add(
            Criterion(
                k,
                null,
                "like $v",
                true,
                true
            )
        ) }
    }

    fun Sqls<T>.andNotLike(k: KMutableProperty1<T, *>, v: String) {
        this.andNotLike(k.name, dealLikeValue(v))
    }

    @PublishedApi
    internal fun andNotLike(k: String, v: Any?) {
        v?.let { this.criteria.criterions.add(
            Criterion(
                k,
                null,
                "not like $v",
                true,
                true
            )
        ) }
    }

    fun Sqls<T>.orIsNull(vararg k: KMutableProperty1<T, String?>) {
        k.forEach {
            this.orIsNull(it.name)
        }
    }

    private fun orIsNull(k: String) {
        this.criteria.criterions.add(Criterion(k, null, "is null", false, true))
    }

    fun Sqls<T>.orIsNotNull(vararg k: KMutableProperty1<T, String?>) {
        k.forEach {
            this.orIsNotNull(it.name)
        }
    }

    private fun orIsNotNull(k: String) {
        this.criteria.criterions.add(Criterion(k, null, "is not null", false, true))
    }


    fun Sqls<T>.orEqualTo(k: KMutableProperty1<T, *>, v: String) {
        this.orEqualTo(k.name, v)
    }

    inline fun <reified T> Sqls<T>.orEqualTo(op: T.() -> Unit = {}) {
        for ((k, v) in this.getValues(op)) {
            this.orEqualTo(k, v)
        }
    }

    @PublishedApi
    internal fun orEqualTo(k: String, v: Any) {
        this.criteria.criterions.add(Criterion(k, v, "=", false))
    }


    fun Sqls<T>.orNotEqualTo(k: KMutableProperty1<T, *>, v: String) {
        this.orNotEqualTo(k.name, v)
    }

    inline fun <reified T> Sqls<T>.orNotEqualTo(op: T.() -> Unit = {}) {
        for ((k, v) in this.getValues(op)) {
            this.orNotEqualTo(k, v)
        }
    }

    @PublishedApi
    internal fun orNotEqualTo(k: String, v: Any) {
        this.criteria.criterions.add(Criterion(k, v, "<>", false))
    }


    fun Sqls<T>.orGreaterThan(k: KMutableProperty1<T, *>, v: String) {
        this.orGreaterThan(k.name, v)
    }

    inline fun <reified T> Sqls<T>.orGreaterThan(op: T.() -> Unit = {}) {
        for ((k, v) in this.getValues(op)) {
            this.orGreaterThan(k, v)
        }
    }

    @PublishedApi
    internal fun orGreaterThan(k: String, v: Any) {
        this.criteria.criterions.add(Criterion(k, v, ">", false))
    }


    fun Sqls<T>.orGreaterThanOrEqualTo(k: KMutableProperty1<T, *>, v: String) {
        this.orGreaterThanOrEqualTo(k.name, v)
    }

    inline fun <reified T> Sqls<T>.orGreaterThanOrEqualTo(op: T.() -> Unit = {}) {
        for ((k, v) in this.getValues(op)) {
            this.orGreaterThanOrEqualTo(k, v)
        }
    }

    @PublishedApi
    internal fun orGreaterThanOrEqualTo(k: String, v: Any) {
        this.criteria.criterions.add(Criterion(k, v, ">=", false))
    }


    fun Sqls<T>.orLessThan(k: KMutableProperty1<T, *>, v: String) {
        this.orLessThan(k.name, v)
    }

    inline fun <reified T> Sqls<T>.orLessThan(op: T.() -> Unit = {}) {
        for ((k, v) in this.getValues(op)) {
            this.orLessThan(k, v)
        }
    }

    @PublishedApi
    internal fun orLessThan(k: String, v: Any) {
        this.criteria.criterions.add(Criterion(k, v, "<", false))

    }

    fun Sqls<T>.orLessThanOrEqualTo(k: KMutableProperty1<T, *>, v: String) {
        this.orLessThanOrEqualTo(k.name, v)
    }

    inline fun <reified T> Sqls<T>.orLessThanOrEqualTo(op: T.() -> Unit = {}) {
        for ((k, v) in this.getValues(op)) {
            this.orLessThanOrEqualTo(k, v)
        }
    }

    @PublishedApi
    internal fun orLessThanOrEqualTo(k: String, value: Any) {
        this.criteria.criterions.add(Criterion(k, value, "<=", false))
    }

    fun Sqls<T>.orIn(k: KMutableProperty1<T, *>, v: Iterable<*>) {
        this.orIn(k.name, v)
    }

    @PublishedApi
    internal fun orIn(k: String, values: Iterable<*>) {
        this.criteria.criterions.add(Criterion(k, values, "in", false))
    }


    fun Sqls<T>.orNotIn(k: KMutableProperty1<T, *>, v: Iterable<*>) {
        this.orNotIn(k.name, v)
    }

    @PublishedApi
    internal fun orNotIn(k: String, values: Iterable<*>) {
        this.criteria.criterions.add(Criterion(k, values, "not in", false))
    }

    fun Sqls<T>.orBetween(k: KMutableProperty1<T, *>, v1: Any?, v2: Any?) {
        if (v1 == null && v2 != null) {
            this.orLessThanOrEqualTo(k.name, v2)
        } else if (v1 != null && v2 == null) {
            this.orGreaterThanOrEqualTo(k.name, v1)
        } else if (v1 != null && v2 != null) {
            this.orBetween(k.name, v1, v2)
        }
    }

    @PublishedApi
    internal fun orBetween(k: String, v1: Any, v2: Any) {
        this.criteria.criterions.add(
            Criterion(
                k,
                null,
                "between $v1 and $v2",
                false,
                true
            )
        )
    }

    fun Sqls<T>.orNotBetween(k: KMutableProperty1<T, *>, v1: Any, v2: Any) {
        this.orNotBetween(k.name, v1, v2)
    }

    @PublishedApi
    internal fun orNotBetween(k: String, v1: Any, v2: Any) {
        this.criteria.criterions.add(
            Criterion(
                k,
                null,
                "not between $v1 and $v2",
                false,
                true
            )
        )
    }

    fun Sqls<T>.orLike(k: KMutableProperty1<T, *>, v: String?) {
        this.orLike(k.name, dealLikeValue(v))
    }

    inline fun <reified T> Sqls<T>.orLike(op: T.() -> Unit = {}) {
        for ((k, v) in this.getValues(op)) {
            this.orLike(k, dealLikeValue(v.toString()))
        }
    }

    @PublishedApi
    internal fun orLike(k: String, v: Any?) {
        v?.let { this.criteria.criterions.add(
            Criterion(
                k,
                null,
                "like $v",
                false,
                true
            )
        ) }
    }

    fun Sqls<T>.orNotLike(k: KMutableProperty1<T, *>, v: String) {
        this.orNotLike(k.name, dealLikeValue(v))
    }

    @PublishedApi
    internal fun orNotLike(k: String, v: Any?) {
        v?.let { this.criteria.criterions.add(
            Criterion(
                k,
                null,
                "not like $v",
                false,
                true
            )
        ) }
    }

    class Criteria {
        var andOr: String? = null
        val criterions = arrayListOf<Criterion>()
    }

    class Criterion(//列名（支持json）
        var property: String,//value（between功能转移到condition）
        var value: Any?,
        var condition: String,//条件(>)或自定义条件(between 1 and 2)
        var andOr: Boolean,//and or（true是and，false为or）
        var customCondition: Boolean = false//是否为自定义条件
    )
}

class Example<T> constructor(
    private var entityClass: Class<*>
) : Sqls<T>() {
    //过滤为Null的值
    private var filterNull: Boolean = true
    //属性是否必须存在（为json时需要设置为false）
    private var exists: Boolean = true
    private var table = EntityHelper.getEntityTable(entityClass)
    private var propertyMap = table.propertyMap
    private var orderByClause = StringBuilder()
    private var distinct: Boolean = false
    private var forUpdate: Boolean = false
    private var selectColumns = linkedSetOf<String>()
    private var excludeColumns = linkedSetOf<String>()
    private val sqlsCriteria = ArrayList<Criteria>(2)
    private var oredCriteria: MutableList<tk.mybatis.mapper.entity.Example.Criteria>? = null
    private var tableName: String? = null

    fun Example<T>.filterNull(filterNull: Boolean = true) {
        this.filterNull = filterNull
    }

    fun Example<T>.exists(exists: Boolean = true) {
        this.exists = exists
    }

    fun Example<T>.distinct(distinct: Boolean = true) {
        this.distinct = distinct
    }

    fun Example<T>.forUpdate(forUpdate: Boolean = true) {
        this.forUpdate = forUpdate
    }

    fun Example<T>.select(vararg k: KMutableProperty1<T, *>) {
        if (k.isNotEmpty()) {
            for (property in k) {
                if (this.propertyMap.containsKey(property.name)) {
                    propertyMap[property.name]?.column?.let { this.selectColumns.add(it) }
                } else {
                    throw MapperException("当前实体类不包含名为" + property.name + "的属性!")
                }
            }
        }
    }

    fun Example<T>.notSelect(vararg properties: String) {
        if (properties.isNotEmpty()) {
            for (property in properties) {
                if (propertyMap.containsKey(property)) {
                    propertyMap[property]?.column?.let { this.excludeColumns.add(it) }
                } else {
                    throw MapperException("当前实体类不包含名为" + property + "的属性!")
                }
            }
        }
    }

    /**
     * 作用？
     */
    fun Example<T>.from(tableName: String) {
        this.tableName = tableName
    }

    fun Example<T>.where(op: Sqls<T>.() -> Unit = {}) {
        val sqls = Sqls<T>()
        op(sqls)
        val criteria = sqls.criteria
        criteria.andOr = "and"
        this.sqlsCriteria.add(criteria)
    }

    fun Example<T>.andWhere(op: Sqls<T>.() -> Unit = {}) {
        where(op)
    }

    fun Example<T>.orWhere(op: Sqls<T>.() -> Unit = {}) {
        val sqls = Sqls<T>()
        op(sqls)
        val criteria = sqls.criteria
        criteria.andOr = "or"
        this.sqlsCriteria.add(criteria)
    }

    fun Example<T>.orderByAsc(vararg k: KMutableProperty1<T, *>) {
        contactOrderByClause(" Asc", *k)
    }

    fun Example<T>.orderByAsc(k: String) {
        orderByClause.append(",$k Asc")
    }

    fun Example<T>.orderByAsc(k: List<String>) {
        if(k.isNotEmpty()){
            orderByClause.append(",${StringUtils.join(k,",")} Asc")
        }
    }

    fun Example<T>.orderByDesc(vararg k: KMutableProperty1<T, *>) {
        contactOrderByClause(" Desc", *k)
    }


    fun Example<T>.orderByDesc(k: String) {
        orderByClause.append(",$k Desc")
    }

    fun Example<T>.orderByDesc(k: List<String>) {
        if(k.isNotEmpty()){
            orderByClause.append(",${StringUtils.join(k,",")} Desc")
        }
    }

    private fun contactOrderByClause(order: String, vararg k: KMutableProperty1<T, *>) {
        val columns = StringBuilder()
        for (property in k) {
            val column = propertyforOderBy(property.name)
            columns.append(",").append(column)
        }
        columns.append(order)
        if (columns.isNotEmpty()) {
            orderByClause.append(columns)
        }
    }
//    inline fun <reified T> Example<T>.andEqualTo(op: T.() -> Unit = {}) {
//
//    }

    fun build(): tk.mybatis.mapper.entity.Example {
        //最外层加一层默认and
        val c = this.criteria
        c.andOr = "and"
        this.sqlsCriteria.add(c)
        //build
        this.oredCriteria = ArrayList()
        val example = tk.mybatis.mapper.entity.Example(entityClass)
        for (criteria in sqlsCriteria) {
            //有三个参数使用默认
            val exampleCriteria = example.createCriteria()
            exampleCriteria.andOr = criteria.andOr
            for (criterion in criteria.criterions) {
                val value = criterion.value
                val customCondition=criterion.customCondition
                //在不是自定义condition时过滤value为NULL
                if (value != null && filterNull || customCondition) {
                    transformCriterion(
                        exampleCriteria,
                        criterion.condition,
                        criterion.property,
                        value,
                        criterion.andOr,
                        customCondition
                    )
                }
            }
            oredCriteria!!.add(exampleCriteria)
        }
        if (this.orderByClause.isNotEmpty()) {
            this.orderByClause = StringBuilder(this.orderByClause.substring(1, this.orderByClause.length))
        }
        val java = example::class.java
        val clazz = this::class.java
        for (field in clazz.declaredFields) {
            field.isAccessible = true
            if (field.name == "sqlsCriteria" || field.name == "filterNull") {
                continue
            }
            var value = field.get(this)
            if (field.name == "orderByClause") {
                value = if (value.toString().isEmpty()) {
                    null
                } else {
                    value.toString()
                }
            }
            val f = java.getDeclaredField(field.name)
            f.isAccessible = true
            f.set(example, value)
        }
        return example
    }

    private fun transformCriterion(
        exampleCriteria: tk.mybatis.mapper.entity.Example.Criteria,
        condition: String,
        property: String,
        value: Any?,
        andOr: Boolean,
        customCondition: Boolean
    ) {
        val clazz = tk.mybatis.mapper.entity.Example.Criteria::class.java.superclass
        if (customCondition) {
            if (andOr) {
                val method = ReflectionUtils.findMethod(clazz, "addCriterion", String::class.java)!!
                ReflectionUtils.makeAccessible(method)
                method.invoke(exampleCriteria, column(property) + " " + condition)
            } else {
                val method = ReflectionUtils.findMethod(clazz, "addOrCriterion", String::class.java)!!
                ReflectionUtils.makeAccessible(method)
                method.invoke(exampleCriteria, column(property) + " " + condition)
            }
        } else {
            if (andOr) {
                val method = ReflectionUtils.findMethod(
                    clazz,
                    "addCriterion",
                    String::class.java,
                    Any::class.java,
                    String::class.java
                )!!
                ReflectionUtils.makeAccessible(method)
                method.invoke(exampleCriteria, column(property) + " " + condition, value, property(property))
            } else {
                val method = ReflectionUtils.findMethod(
                    clazz,
                    "addOrCriterion",
                    String::class.java,
                    Any::class.java,
                    String::class.java
                )!!
                ReflectionUtils.makeAccessible(method)
                method.invoke(exampleCriteria, column(property) + " " + condition, value, property(property))
            }
        }
    }

    private fun column(property: String?): String? {
        return when {
            propertyMap.containsKey(property) -> propertyMap[property]?.column
            exists -> throw MapperException("当前实体类不包含名为" + property + "的属性!")
            else -> property
        }
    }

    private fun property(property: String?): String? {
        return when {
            propertyMap.containsKey(property) -> property
            exists -> throw MapperException("当前实体类不包含名为" + property + "的属性!")
            else -> property
        }
    }

    private fun propertyforOderBy(p: String): String {
        var property = p
        if (StringUtil.isEmpty(property) || StringUtil.isEmpty(property.trim { it <= ' ' })) {
            throw MapperException("接收的property为空！")
        }
        property = property.trim { it <= ' ' }
        if (!propertyMap.containsKey(property)) {
            throw MapperException("当前实体类不包含名为" + property + "的属性!")
        }
        return propertyMap[property]!!.column
    }
}
