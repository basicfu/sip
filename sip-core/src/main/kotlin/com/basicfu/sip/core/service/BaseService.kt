package com.basicfu.sip.core.service

import com.basicfu.sip.core.mapper.CustomMapper
import com.basicfu.sip.core.model.BaseVo
import com.github.pagehelper.PageHelper
import com.github.pagehelper.PageInfo
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import tk.mybatis.mapper.entity.Example
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
    inline fun <reified R> to(obj: T): R {
        val clazz = R::class.java
        val instance = clazz.newInstance()
        BeanUtils.copyProperties(obj, instance)
        return instance
    }

    /**
     * 集合对象copy并返回
     * 如果T和R是同一个类型是否能优化省性能
     */
    inline fun <reified R> to(list: List<T>): List<R> {
        val clazz = R::class.java
        val result = arrayListOf<R>()
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
     * 分页查询，返回指定的类型集合，自动从request里获取分页信息
     * 无条件
     */
    inline fun <reified R> selectPage(): PageInfo<R> {
        val page = BaseVo().setInfo()
        PageHelper.startPage<Any>(page.pageNum, page.pageSize)
        val result = mapper.selectAll()
        val to = to<R>(result)
        return PageInfo(to)
    }
    /**
     * 分页查询，返回指定的类型集合，自动从request里获取分页信息
     * 根据指定条件
     */
    inline fun <reified R> selectPage(example: Example): PageInfo<R> {
        val page = BaseVo().setInfo()
        PageHelper.startPage<Any>(page.pageNum, page.pageSize)
        val result = mapper.selectByExample(example)
        val to = to<R>(result)
        return PageInfo(to)
    }
}