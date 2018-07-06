package com.basicfu.sip.core.mapper

import org.springframework.util.ReflectionUtils
import tk.mybatis.mapper.MapperException
import tk.mybatis.mapper.mapperhelper.EntityHelper
import tk.mybatis.mapper.util.StringUtil
import kotlin.reflect.KMutableProperty1


inline fun <reified T> generate(op: T.() -> Unit = {}): T {
    val instance = T::class.java.newInstance()
    op(instance)
    return instance
}

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

    fun Sqls<T>.andIsNull(vararg k: KMutableProperty1<T, String?>) {
        k.forEach {
            this.andIsNull(it.name)
        }
    }

    private fun andIsNull(k: String) {
        this.criteria.criterions.add(Criterion(k, "is null", "and"))
    }

    fun Sqls<T>.andIsNotNull(vararg k: KMutableProperty1<T, String?>) {
        k.forEach {
            this.andIsNotNull(it.name)
        }
    }

    private fun andIsNotNull(k: String) {
        this.criteria.criterions.add(Criterion(k, "is not null", "and"))
    }


    fun Sqls<T>.andEqualTo(k: KMutableProperty1<T, *>, v: String?) {
        this.andEqualTo(k.name, v)
    }

    inline fun <reified T> Sqls<T>.andEqualTo(op: T.() -> Unit = {}) {
        for ((k, v) in this.getValues(op)) {
            this.andEqualTo(k, v)
        }
    }

    @PublishedApi
    internal fun andEqualTo(k: String, v: Any?) {
        this.criteria.criterions.add(Criterion(k, v, "=", "and"))
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
        this.criteria.criterions.add(Criterion(k, v, "<>", "and"))
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
        this.criteria.criterions.add(Criterion(k, v, ">", "and"))
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
        this.criteria.criterions.add(Criterion(k, v, ">=", "and"))
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
        this.criteria.criterions.add(Criterion(k, v, "<", "and"))

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
        this.criteria.criterions.add(Criterion(k, value, "<=", "and"))
    }

    fun Sqls<T>.andIn(k: KMutableProperty1<T, *>, v: Iterable<*>) {
        this.andIn(k.name, v)
    }

    @PublishedApi
    internal fun andIn(k: String, values: Iterable<*>) {
        this.criteria.criterions.add(Criterion(k, values, "in", "and"))
    }


    fun Sqls<T>.andNotIn(k: KMutableProperty1<T, *>, v: Iterable<*>) {
        this.andNotIn(k.name, v)
    }

    @PublishedApi
    internal fun andNotIn(k: String, values: Iterable<*>) {
        this.criteria.criterions.add(Criterion(k, values, "not in", "and"))
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

    @PublishedApi
    internal fun andBetween(k: String, v1: Any, v2: Any) {
        this.criteria.criterions.add(Criterion(k, v1, v2, "between", "and"))
    }

    fun Sqls<T>.andNotBetween(k: KMutableProperty1<T, *>, v1: Any, v2: Any) {
        this.andNotBetween(k.name, v1, v2)
    }

    @PublishedApi
    internal fun andNotBetween(k: String, v1: Any, v2: Any) {
        this.criteria.criterions.add(Criterion(k, v1, v2, "not between", "and"))
    }

    fun Sqls<T>.andLike(k: KMutableProperty1<T, *>, v: String?) {
        this.andLike(k.name, dealLikeValue(v))
    }

    inline fun <reified T> Sqls<T>.andLike(op: T.() -> Unit = {}) {
        for ((k, v) in this.getValues(op)) {
            this.andLike(k, v)
        }
    }

    @PublishedApi
    internal fun andLike(k: String, v: Any?) {
        this.criteria.criterions.add(Criterion(k, v, "like", "and"))
    }

    fun Sqls<T>.andNotLike(k: KMutableProperty1<T, *>, v: String) {
        this.andNotLike(k.name, dealLikeValue(v))
    }

    @PublishedApi
    internal fun andNotLike(k: String, v: Any?) {
        this.criteria.criterions.add(Criterion(k, v, "not like", "and"))
    }

    fun Sqls<T>.orIsNull(vararg k: KMutableProperty1<T, String?>) {
        k.forEach {
            this.orIsNull(it.name)
        }
    }

    private fun orIsNull(k: String) {
        this.criteria.criterions.add(Criterion(k, "is null", "or"))
    }

    fun Sqls<T>.orIsNotNull(vararg k: KMutableProperty1<T, String?>) {
        k.forEach {
            this.orIsNotNull(it.name)
        }
    }

    private fun orIsNotNull(k: String) {
        this.criteria.criterions.add(Criterion(k, "is not null", "or"))
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
        this.criteria.criterions.add(Criterion(k, v, "=", "or"))
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
        this.criteria.criterions.add(Criterion(k, v, "<>", "or"))
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
        this.criteria.criterions.add(Criterion(k, v, ">", "or"))
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
        this.criteria.criterions.add(Criterion(k, v, ">=", "or"))
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
        this.criteria.criterions.add(Criterion(k, v, "<", "or"))

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
        this.criteria.criterions.add(Criterion(k, value, "<=", "or"))
    }

    fun Sqls<T>.orIn(k: KMutableProperty1<T, *>, v: Iterable<*>) {
        this.orIn(k.name, v)
    }

    @PublishedApi
    internal fun orIn(k: String, values: Iterable<*>) {
        this.criteria.criterions.add(Criterion(k, values, "in", "or"))
    }


    fun Sqls<T>.orNotIn(k: KMutableProperty1<T, *>, v: Iterable<*>) {
        this.orNotIn(k.name, v)
    }

    @PublishedApi
    internal fun orNotIn(k: String, values: Iterable<*>) {
        this.criteria.criterions.add(Criterion(k, values, "not in", "or"))
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
        this.criteria.criterions.add(Criterion(k, v1, v2, "between", "or"))
    }

    fun Sqls<T>.orNotBetween(k: KMutableProperty1<T, *>, v1: Any, v2: Any) {
        this.orNotBetween(k.name, v1, v2)
    }

    @PublishedApi
    internal fun orNotBetween(k: String, v1: Any, v2: Any) {
        this.criteria.criterions.add(Criterion(k, v1, v2, "not between", "or"))
    }

    fun Sqls<T>.orLike(k: KMutableProperty1<T, *>, v: String) {
        this.orLike(k.name, dealLikeValue(v))
    }

    @PublishedApi
    internal fun orLike(k: String, v: Any?) {
        this.criteria.criterions.add(Criterion(k, v, "like", "or"))
    }

    fun Sqls<T>.orNotLike(k: KMutableProperty1<T, *>, v: String) {
        this.orNotLike(k.name, dealLikeValue(v))
    }

    @PublishedApi
    internal fun orNotLike(k: String, v: Any?) {
        this.criteria.criterions.add(Criterion(k, v, "not like", "or"))
    }


    private fun dealLikeValue(v: String?): String? {
        if (v == null) {
            return null
        }
        var value = v
        if (value.contains("%") || value.contains("_")) {
            value = value.replace("%", "/%").replace("_", "/_")
            value = "%$value%"
            value += " escape '/'"
        } else {
            value = "%$value%"
        }
        return value
    }


    class Criteria {
        var andOr: String? = null
        val criterions = arrayListOf<Criterion>()
    }

    class Criterion {
        var property: String? = null
        var value: Any? = null
        var secondValue: Any? = null
        var condition: String? = null
        var andOr: String? = null
        val values: Array<Any>
            get() = if (value != null) {
                if (secondValue != null) {
                    arrayOf(value!!, secondValue!!)
                } else {
                    arrayOf(value!!)
                }
            } else {
                arrayOf()
            }

        constructor(property: String, condition: String, andOr: String) {
            this.property = property
            this.condition = condition
            this.andOr = andOr
        }


        constructor(property: String, value: Any?, condition: String, andOr: String) {
            this.property = property
            this.value = value
            this.condition = condition
            this.andOr = andOr
        }

        constructor(property: String, value1: Any, value2: Any, condition: String, andOr: String) {
            this.property = property
            this.value = value1
            this.secondValue = value2
            this.condition = condition
            this.andOr = andOr
        }
    }

}

class Example<T> @JvmOverloads constructor(
    private var entityClass: Class<*>,
    //过滤为Null的值
    private var filterNull:Boolean=true,
    //值是否允许为Null
    private var notNull: Boolean = false,
    //判断属性是否必须存在
    private var exists: Boolean = true
    ) : Sqls<T>() {
    private var table = EntityHelper.getEntityTable(entityClass)
    private var propertyMap = table.propertyMap
    private var orderByClause = StringBuilder()
    var distinct: Boolean = false
    var forUpdate: Boolean = false
    private var selectColumns = linkedSetOf<String>()
    private var excludeColumns = linkedSetOf<String>()
    private val sqlsCriteria = ArrayList<Criteria>(2)
    private var oredCriteria: MutableList<tk.mybatis.mapper.entity.Example.Criteria>? = null
    private var tableName: String? = null

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

    fun Example<T>.orderByDesc(vararg k: KMutableProperty1<T, *>) {
        contactOrderByClause(" Desc", *k)
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
                val condition = criterion.condition
                val andOr = criterion.andOr
                val property = criterion.property
                val values = criterion.values
                //过滤value为NULL的
                if(criterion.value!=null){
                    transformCriterion(exampleCriteria, condition, property, values, andOr)
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
            if (field.name == "sqlsCriteria"||field.name == "filterNull") {
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
        condition: String?,
        property: String?,
        values: Array<Any>,
        andOr: String?
    ) {
        val clazz = tk.mybatis.mapper.entity.Example.Criteria::class.java.superclass
        if (values.isEmpty()) {
            if ("and" == andOr) {
                val method = ReflectionUtils.findMethod(clazz, "addCriterion", String::class.java)
                ReflectionUtils.makeAccessible(method)
                method.invoke(exampleCriteria, column(property) + " " + condition)
//                exampleCriteria.addCriterion(column(property) + " " + condition)
            } else {
                val method = ReflectionUtils.findMethod(clazz, "addOrCriterion", String::class.java)
                ReflectionUtils.makeAccessible(method)
                method.invoke(exampleCriteria, column(property) + " " + condition)
//                exampleCriteria.addOrCriterion(column(property) + " " + condition)
            }
        } else if (values.size == 1) {
            if ("and" == andOr) {
                val method = ReflectionUtils.findMethod(
                    clazz,
                    "addCriterion",
                    String::class.java,
                    Any::class.java,
                    String::class.java
                )
                ReflectionUtils.makeAccessible(method)
                method.invoke(exampleCriteria, column(property) + " " + condition, values[0], property(property))
//                exampleCriteria.addCriterion(column(property) + " " + condition, values[0], property(property))
            } else {
                val method = ReflectionUtils.findMethod(
                    clazz,
                    "addOrCriterion",
                    String::class.java,
                    Any::class.java,
                    String::class.java
                )
                ReflectionUtils.makeAccessible(method)
                method.invoke(exampleCriteria, column(property) + " " + condition, values[0], property(property))
//                exampleCriteria.addOrCriterion(column(property) + " " + condition, values[0], property(property))
            }
        } else if (values.size == 2) {
            if ("and" == andOr) {
                val method = ReflectionUtils.findMethod(
                    clazz,
                    "addCriterion",
                    String::class.java,
                    Any::class.java,
                    Any::class.java,
                    String::class.java
                )
                ReflectionUtils.makeAccessible(method)
                method.invoke(
                    exampleCriteria,
                    column(property) + " " + condition,
                    values[0],
                    values[1],
                    property(property)
                )
//                exampleCriteria.addCriterion(column(property) + " " + condition,values[0],values[1],property(property))
            } else {
                val method = ReflectionUtils.findMethod(
                    clazz,
                    "addOrCriterion",
                    String::class.java,
                    Any::class.java,
                    Any::class.java,
                    String::class.java
                )
                ReflectionUtils.makeAccessible(method)
                method.invoke(
                    exampleCriteria,
                    column(property) + " " + condition,
                    values[0],
                    values[1],
                    property(property)
                )
//                exampleCriteria.addOrCriterion(column(property) + " " + condition,values[0],values[1],property(property))
            }
        }
    }

    private fun column(property: String?): String? {
        return when {
            propertyMap.containsKey(property) -> propertyMap[property]?.column
            exists -> throw MapperException("当前实体类不包含名为" + property + "的属性!")
            else -> null
        }
    }

    private fun property(property: String?): String? {
        return when {
            propertyMap.containsKey(property) -> property
            exists -> throw MapperException("当前实体类不包含名为" + property + "的属性!")
            else -> null
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

//    fun Sqls<T>.andEqualTo(op: HashMap<String, Any>.() -> Unit = {}) {
//        val hashMap = HashMap<String, Any>()
//        op(hashMap)
//        hashMap.forEach { k, v ->
//            andEqualTo(k,v)
//        }
//    }
//fun HashMap<String, Any>.kv(a: String, b: String) {
//    put(a, b)
//}
