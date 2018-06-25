package com.basicfu.sip.core.service

import com.basicfu.sip.core.mapper.CustomMapper
import com.basicfu.sip.core.model.Result
import com.basicfu.sip.core.util.HttpUtil.UA.list
import com.github.pagehelper.PageHelper
import com.github.pagehelper.PageInfo
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import tk.mybatis.mapper.entity.Example
import tk.mybatis.mapper.util.Sqls
import tk.mybatis.mapper.weekend.WeekendSqls
import java.lang.invoke.SerializedLambda
import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST")
abstract class BaseService<M : CustomMapper<T>, T> {
    @Autowired
    lateinit var mapper: M

    private val clazz: Class<T> =
        (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<T>

    /**
     * 单个对象copy并返回
     */
    inline fun <reified R> to(obj:T):R{
        val clazz = R::class.java
        val instance = clazz.newInstance()
        BeanUtils.copyProperties(obj, instance)
        return instance
    }

    /**
     * 集合对象copy并返回
     */
    inline fun <reified R> to(list:List<T>):List<R>{
        val clazz = R::class.java
        val result=arrayListOf<R>()
        list.forEach {
            val instance = clazz.newInstance()
            BeanUtils.copyProperties(it, instance)
            result.add(instance)
        }
        return result
    }

    /**
     * 根据ids查询
     */
    fun selectByIds(ids: List<Long>?): MutableList<T> {
        return if (ids?.isNotEmpty() == true) {
            mapper.selectByIds(StringUtils.join(ids, ","))
        } else {
            arrayListOf()
        }
    }

    /**
     * select page
     */
    fun selectPageEqual(equalProperty: Array<Array<*>>, pageNum: Int, pageSize: Int): PageInfo<T> {
        return selectPage(equalProperty, null, null, null, null, null, null, pageNum, pageSize)
    }

    fun selectPageLike(likeProperty: Array<Array<*>>, pageNum: Int, pageSize: Int): PageInfo<T> {
        return selectPage(null, likeProperty, null, null, null, null, null, pageNum, pageSize)
    }

    fun selectPageOrderBy(orderByProperty: Array<Array<*>>, pageNum: Int, pageSize: Int): PageInfo<T> {
        return selectPage(null, null, orderByProperty, null, null, null, null, pageNum, pageSize)
    }

    fun selectPage(pageNum: Int, pageSize: Int): PageInfo<T> {
        return selectPage(null, null, null, null, null, null, null, pageNum, pageSize)
    }

    /**
     * select
     */
    fun selectEqual(equalProperty: Array<Array<*>>): List<T> {
        return select(equalProperty, null, null, null, null, null, null)
    }

    fun selectLike(likeProperty: Array<Array<*>>): List<T> {
        return select(null, likeProperty, null, null, null, null, null)
    }

    fun selectOrderBy(orderByProperty: Array<Array<*>>): List<T> {
        return select(null, null, orderByProperty, null, null, null, null)
    }

    fun select(): List<T> {
        return select(null, null, null, null, null, null, null)
    }

    /**
     * 或like和分页处理后转dto返回
     */
    fun selectOrLike(list: List<String>, keyword: Any?, pageNum: Int, pageSize: Int): PageInfo<Any> {
        PageHelper.startPage<Any>(pageNum, pageSize)
        val poList = PageInfo<Any>(if (list.isEmpty() || keyword == null || keyword == "") {
            mapper.selectAll()
        } else {
            val example = Example(clazz)
            val createCriteria = example.createCriteria()
            list.forEach { e ->
                createCriteria.orLike(e, "%$keyword%")
            }
            mapper.selectByExample(example)
        } as List<*>?)
        val dtoList = mutableListOf<Any>()
        poList.list.forEach { e ->
            val dtoInstance = Class.forName(clazz.name.replace(".po", ".dto") + "Dto").newInstance()
            BeanUtils.copyProperties(e, dtoInstance)
            dtoList.add(dtoInstance)
        }
        poList.list = dtoList
        return poList
    }

    /**
     * 或like处理后转dto返回
     */
    fun selectOrLike(list: List<String>, keyword: Any?): PageInfo<Any> {
        val poList = PageInfo<Any>(if (list.isEmpty() || keyword == null || keyword == "") {
            mapper.selectAll()
        } else {
            val example = Example(clazz)
            val createCriteria = example.createCriteria()
            list.forEach { e ->
                createCriteria.orLike(e, "%$keyword%")
            }
            mapper.selectByExample(example)
        } as List<*>?)
        val dtoList = mutableListOf<Any>()
        poList.list.forEach { e ->
            val dtoInstance = Class.forName(clazz.name.replace(".po", ".dto") + "Dto").newInstance()
            BeanUtils.copyProperties(e, dtoInstance)
            dtoList.add(dtoInstance)
        }
        poList.list = dtoList
        return poList
    }

    fun selectIn(property: String, list: List<*>): List<T> {
        val example = Example(clazz)
        val criteria = example.createCriteria()
        criteria.andIn(property, list)
        return mapper.selectByExample(example)
    }

    /**
     * select page dto
     */
    fun selectPageDtoEqual(equalProperty: Array<Array<*>>, pageNum: Int, pageSize: Int): PageInfo<Any> {
        return selectPageDto(equalProperty, null, null, pageNum, pageSize)
    }

    fun selectPageDtoLike(likeProperty: Array<Array<*>>, pageNum: Int, pageSize: Int): PageInfo<Any> {
        return selectPageDto(null, likeProperty, null, pageNum, pageSize)
    }

    fun selectPageDtoOrderBy(orderByProperty: Array<Array<*>>, pageNum: Int, pageSize: Int): PageInfo<Any> {
        return selectPageDto(null, null, orderByProperty, pageNum, pageSize)
    }

    fun selectPageDto(pageNum: Int, pageSize: Int): PageInfo<Any> {
        return selectPageDto(null, null, null, pageNum, pageSize)
    }

    /**
     * select dto
     */
    fun selectDtoEqual(equalProperty: Array<Array<*>>): List<Any> {
        return selectDto(equalProperty, null, null)
    }

    fun selectDtoLike(likeProperty: Array<Array<*>>): List<Any> {
        return selectDto(null, likeProperty, null)
    }

    fun selectDtoOrderBy(orderByProperty: Array<Array<*>>): List<Any> {
        return selectDto(null, null, orderByProperty)
    }

    fun selectDto(): List<Any> {
        return selectDto(null, null, null)
    }

    /**
     * s
     * 字段值为null不处理
     */
    fun selectDto(
        equalProperty: Array<Array<*>>?,
        likeProperty: Array<Array<*>>?,
        orderByProperty: Array<Array<*>>?
    ): List<Any> {
        val list = select(equalProperty, likeProperty, orderByProperty, null, null, null, null)
        val dtoList = mutableListOf<Any>()
        list.forEach { e ->
            val dtoInstance = Class.forName(clazz.name.replace(".po", ".dto") + "Dto").newInstance()
            BeanUtils.copyProperties(e, dtoInstance)
            dtoList.add(dtoInstance)
        }
        return dtoList
    }

    fun selectDto(whereProperty: Array<Array<*>>?): List<Any> {
        var equalProperty: Array<Array<*>>? = null
        var inProperty: Array<Array<*>>? = null
        var likeProperty: Array<Array<*>>? = null
        var betweenProperty: Array<Array<*>>? = null
        var orderByProperty: Array<Array<*>>? = null
        var andCondition: Array<String>? = null
        var lock: Boolean? = null
        whereProperty?.forEach { e ->
            when (e[0]) {
                "equals" -> equalProperty = e[1] as Array<Array<*>>
                "in" -> inProperty = e[1] as Array<Array<*>>
                "like" -> likeProperty = e[1] as Array<Array<*>>
                "andCondition" -> andCondition = e[1] as Array<String>
                "between" -> betweenProperty = e[1] as Array<Array<*>>
                "orderBy" -> orderByProperty = e[1] as Array<Array<*>>
                "lock" -> lock = e[1] as Boolean
            }
        }
        val list = select(equalProperty, likeProperty, orderByProperty, betweenProperty, inProperty, andCondition, lock)
        val dtoList = mutableListOf<Any>()
        list.forEach { e ->
            val dtoInstance = Class.forName(clazz.name.replace(".po", ".dto") + "Dto").newInstance()
            BeanUtils.copyProperties(e, dtoInstance)
            dtoList.add(dtoInstance)
        }
        return dtoList
    }

    fun selectPageDto(
        equalProperty: Array<Array<*>>?,
        likeProperty: Array<Array<*>>?,
        orderByProperty: Array<Array<*>>?,
        pageNum: Int,
        pageSize: Int
    ): PageInfo<Any> {
        val pageList = selectPage(
            equalProperty,
            likeProperty,
            orderByProperty,
            null,
            null,
            null,
            null,
            pageNum,
            pageSize
        ) as PageInfo<Any>
        val dtoList = mutableListOf<Any>()
        pageList.list.forEach { e ->
            val dtoInstance = Class.forName(clazz.name.replace(".po", ".dto") + "Dto").newInstance()
            BeanUtils.copyProperties(e, dtoInstance)
            dtoList.add(dtoInstance)
        }
        pageList.list = dtoList
        return pageList
    }

    fun selectPageDto(whereProperty: Array<Array<*>>?, pageNum: Int, pageSize: Int): PageInfo<Any> {
        var equalProperty: Array<Array<*>>? = null
        var inProperty: Array<Array<*>>? = null
        var likeProperty: Array<Array<*>>? = null
        var betweenProperty: Array<Array<*>>? = null
        var orderByProperty: Array<Array<*>>? = null
        var andCondition: Array<String>? = null
        var lock: Boolean? = null
        whereProperty?.forEach { e ->
            when (e[0]) {
                "equals" -> equalProperty = e[1] as Array<Array<*>>
                "in" -> inProperty = e[1] as Array<Array<*>>
                "like" -> likeProperty = e[1] as Array<Array<*>>
                "andCondition" -> andCondition = e[1] as Array<String>
                "between" -> betweenProperty = e[1] as Array<Array<*>>
                "orderBy" -> orderByProperty = e[1] as Array<Array<*>>
                "lock" -> lock = e[1] as Boolean
            }
        }
        val pageList = selectPage(
            equalProperty,
            likeProperty,
            orderByProperty,
            betweenProperty,
            inProperty,
            andCondition,
            lock,
            pageNum,
            pageSize
        ) as PageInfo<Any>
        val dtoList = mutableListOf<Any>()
        pageList.list.forEach { e ->
            val dtoInstance = Class.forName(clazz.name.replace(".po", ".dto") + "Dto").newInstance()
            BeanUtils.copyProperties(e, dtoInstance)
            dtoList.add(dtoInstance)
        }
        pageList.list = dtoList
        return pageList
    }

    fun select(whereProperty: Array<Array<*>>?): List<T> {
        var equalProperty: Array<Array<*>>? = null
        var inProperty: Array<Array<*>>? = null
        var likeProperty: Array<Array<*>>? = null
        var betweenProperty: Array<Array<*>>? = null
        var orderByProperty: Array<Array<*>>? = null
        var andCondition: Array<String>? = null
        var lock: Boolean? = null
        whereProperty?.forEach { e ->
            when (e[0]) {
                "equals" -> equalProperty = e[1] as Array<Array<*>>
                "in" -> inProperty = e[1] as Array<Array<*>>
                "like" -> likeProperty = e[1] as Array<Array<*>>
                "andCondition" -> andCondition = e[1] as Array<String>
                "between" -> betweenProperty = e[1] as Array<Array<*>>
                "orderBy" -> orderByProperty = e[1] as Array<Array<*>>
                "lock" -> lock = e[1] as Boolean
            }
        }
        return select(equalProperty, likeProperty, orderByProperty, betweenProperty, inProperty, andCondition, lock)
    }

    fun select(
        equalProperty: Array<Array<*>>?, likeProperty: Array<Array<*>>?, orderByProperty: Array<Array<*>>?
        , betweenProperty: Array<Array<*>>?, inProperty: Array<Array<*>>?, andCondition: Array<String>?, lock: Boolean?
    ): List<T> {
        if (equalProperty == null && likeProperty == null && orderByProperty == null) {
            return mapper.selectAll()
        } else {
            val example = generateExample(
                equalProperty,
                likeProperty,
                orderByProperty,
                betweenProperty,
                inProperty,
                andCondition,
                lock
            )
            return mapper.selectByExample(example)
        }
    }

    fun selectPage(
        equalProperty: Array<Array<*>>?,
        likeProperty: Array<Array<*>>?
        ,
        orderByProperty: Array<Array<*>>?,
        between: Array<Array<*>>?,
        inProperty: Array<Array<*>>?,
        andCondition: Array<String>?
        ,
        lock: Boolean?,
        pageNum: Int,
        pageSize: Int
    ): PageInfo<T> {
        PageHelper.startPage<Any>(pageNum, pageSize)
        if (equalProperty == null && likeProperty == null && orderByProperty == null && between == null && inProperty == null) {
            return PageInfo(mapper.selectAll())
        } else {
            return PageInfo(
                mapper.selectByExample(
                    generateExample(
                        equalProperty,
                        likeProperty,
                        orderByProperty,
                        between,
                        inProperty,
                        andCondition,
                        lock
                    )
                )
            )
        }
    }

    private fun generateExample(
        equalProperty: Array<Array<*>>?, likeProperty: Array<Array<*>>?
        , orderByProperty: Array<Array<*>>?, between: Array<Array<*>>?, inProperty: Array<Array<*>>?,
        andCondition: Array<String>?, lock: Boolean?
    ): Example {
        val example = Example(clazz)
//        val build = Example.builder(clazz).andWhere(Sqls.custom().andEqualTo("a", "b")).build()
        val criteria = example.createCriteria()
        equalProperty?.forEach { e ->
            if (e[1] != null) {
                criteria.andEqualTo(e[0].toString(), e[1])
            }
        }
//        criteria.andCondition()
        inProperty?.forEach { e ->
            if (e[1] != null) {
                val list = e[1] as List<Any?>?
                if (list!!.isNotEmpty()) {
                    criteria.andIn(e[0].toString(), list)
                }
            }
        }
        likeProperty?.forEach { e ->
            if (e[1] != null) {
                var v = e[1].toString()
                if (v.contains("%") || v.contains("_")) {
                    v = v.replace("%", "/%").replace("_", "/_")
                    v = "%$v%"
                    v += " escape '/'"
                } else {
                    v = "%$v%"
                }
                criteria.andLike(e[0].toString(), v)
            }
        }
        andCondition?.forEach { e ->
            criteria.andCondition("($e)")
        }
        between?.forEach { e ->
            if (e[0] != null && (e[1] != null || e[2] != null)) {
                if (e[1] == null && e[2] != null) {
                    criteria.andLessThanOrEqualTo(e[0].toString(), e[2])
                } else if (e[1] != null && e[2] == null) {
                    criteria.andGreaterThanOrEqualTo(e[0].toString(), e[1])
                } else {
                    criteria.andBetween(e[0].toString(), e[1], e[2])
                }
            }
        }
        orderByProperty?.forEach { e ->
            if (e[1].toString().toLowerCase() == "asc") {
                example.orderBy(e[0].toString()).asc()
            } else {
                example.orderBy(e[0].toString()).desc()
            }
        }
        if (lock != null) {
            example.isForUpdate = lock
        }
        return example
    }

    fun selectCount(): Int {
        return selectCount(null, null)
    }

    fun selectCountEqual(vararg equalProperty: Array<*>): Int {
        return selectCount(equalProperty.map { it }.toTypedArray(), null)
    }

    fun selectCountLike(vararg likeProperty: Array<Array<*>>): Int {
        return selectCount(null, arrayOf(likeProperty))
    }

    /**
     * 根据不同的字段，不同的value查询条数
     */
    fun selectCount(equalProperty: Array<Array<*>>?, likeProperty: Array<Array<*>>?): Int {
        return if (equalProperty == null && likeProperty == null) {
            mapper.selectCount(null)
        } else {
            mapper.selectCountByExample(generateExample(equalProperty, likeProperty, null, null, null, null, null))
        }
    }

    /**
     * 根据不同的字段，不同的value查询对象
     */
    fun selectOne(vararg property: Array<Any?>): T? {
        val instance = clazz.newInstance()
        property.forEach { e ->
            val field = clazz.getDeclaredField(e[0].toString())
            field.isAccessible = true
            field.set(instance, e[1])
        }
        return mapper.selectOne(instance)
    }

    /**
     * vo选择性插入
     */
    fun insertVoSelective(vo: Any): Int {
        val instance = clazz.newInstance()
        BeanUtils.copyProperties(vo, instance)
        return mapper.insertSelective(instance)
    }

    /**
     * po选择性插入-如果没有主键会返回添加后的主键
     */
    fun insertPoSelective(po: T): Int {
        return mapper.insertSelective(po)
    }

    /**
     * po插入并返回主键
     */
    fun insertPoUseGeneratedKeys(po: T): Int {
        return mapper.insertUseGeneratedKeys(po)
    }

    /**
     * po批量插入
     */
    fun insertPoList(po: List<T>): Int {
        return mapper.insertList(po)
    }

    /**
     * 根据主键po选择性更新其他值
     */
    fun updatePoByPrimaryKeySelective(po: T): Int {
        return mapper.updateByPrimaryKeySelective(po)
    }

    /**
     * 根据主键vo选择性更新其他值
     */
    fun updateVoByPrimaryKeySelective(vo: Any): Int {
        val instance = clazz.newInstance()
        BeanUtils.copyProperties(vo, instance)
        return mapper.updateByPrimaryKeySelective(instance)
    }

    /**
     * 根据自定义字段值更新对象
     */
    fun updateVoBySelectiveEqual(vo: Any, equalProperty: Array<Array<*>>): Int {
        val example = Example(clazz)
        val criteria = example.createCriteria()
        val java = vo::class.java
        val instance = clazz.newInstance()
        equalProperty.forEach { e ->
            val field = java.getDeclaredField(e[0].toString())
            field.isAccessible = true
            field.set(vo, null)
            criteria.andEqualTo(e[0].toString(), e[1].toString())
        }
        BeanUtils.copyProperties(vo, instance)
        return mapper.updateByExampleSelective(instance, example)
    }

    fun updatePoBySelectiveEqual(po: T, equalProperty: Array<Array<*>>): Int {
        val example = Example(clazz)
        val criteria = example.createCriteria()
        equalProperty.forEach { e ->
            criteria.andEqualTo(e[0].toString(), e[1].toString())
        }
        return mapper.updateByExampleSelective(po, example)
    }

    fun deleteInAndEqual(inProperty: Array<*>?, equalProperty: Array<*>?): Int {
        val example = Example(clazz)
        val criteria = example.createCriteria()
        if (inProperty != null) {
            criteria.andIn(inProperty[0].toString(), inProperty[1] as List<*>)
        }
        if (equalProperty != null) {
            criteria.andEqualTo(equalProperty[0].toString(), equalProperty[1])
        }
        return mapper.deleteByExample(example)
    }

    fun deleteInAndEqualArray(inProperty: Array<Array<*>>?, equalProperty: Array<Array<*>>?): Int {
        val example = Example(clazz)
        val criteria = example.createCriteria()
        inProperty?.forEach { e ->
            criteria.andIn(e[0].toString(), e[1] as List<*>)
        }
        equalProperty?.forEach { e ->
            criteria.andEqualTo(e[0].toString(), e[1])
        }
        return mapper.deleteByExample(example)
    }

    fun deleteByPrimaryKeyId(id: Long): Int {
        return mapper.deleteByPrimaryKey(id)
    }

    fun deleteByPrimaryKeyIds(ids: List<Long>): Int {
        return when {
            ids.size == 1 -> mapper.deleteByPrimaryKey(ids[0])
            ids.size > 1 -> mapper.deleteByIds(StringUtils.join(ids, ","))
            else -> 0
        }
    }

    fun delete(entity: T) {
        mapper.delete(entity)
    }


//    /**
//     * 输入vo转po处理后并转dto返回
//     */
//    fun select(vo: Any,clazz: Class<T>): List<Any> {
//        val instance = clazz.newInstance()
//        BeanUtils.copyProperties(vo,instance)
//        val java = vo::class.java.superclass
//        val pageNum=java.getDeclaredField("pageNum")
//        pageNum.isAccessible=true
//        val pageSize=java.getDeclaredField("pageSize")
//        pageSize.isAccessible=true
//        PageHelper.startPage<Any>(pageNum.get(vo).toString().toInt(),pageSize.get(vo).toString().toInt())
//        val poList=mapper.select(instance as T)
//        val dtoList= arrayListOf<Any>()
//        poList.forEach { e->
//            val dtoInstance = Class.forName(clazz.name.replace(".po",".dto")+"Dto").newInstance()
//            BeanUtils.copyProperties(e,dtoInstance)
//            dtoList.add(dtoInstance)
//        }
//        return dtoList
//    }
}