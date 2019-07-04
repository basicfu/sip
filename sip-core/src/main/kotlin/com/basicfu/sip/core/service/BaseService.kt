package com.basicfu.sip.core.service

import com.basicfu.sip.core.model.vo.BaseVo
import com.basicfu.sip.core.common.mapper.CustomMapper
import com.github.pagehelper.Page
import com.github.pagehelper.PageHelper
import com.github.pagehelper.PageInfo
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.ReflectionUtils
import tk.mybatis.mapper.entity.Example
import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST")
abstract class BaseService<M : CustomMapper<T>, T> {
    @Autowired
    lateinit var mapper: M
    private val clazz: Class<T> =
        (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<T>

    /**
     * bean copy
     * obj源对象,R目标对象
     */
    inline fun <reified R> to(obj: Any?): R? {
        if (obj == null) {
            return null
        }
        val clazz = R::class.java
        val instance = clazz.newInstance()!!
        BeanUtils.copyProperties(obj, instance)
        return instance
    }

    /**
     * list<bean> copy
     * obj源对象,R目标对象
     * 如果T和R是同一个类型是否能优化省性能
     */
    inline fun <reified R> to(list: List<Any>): MutableList<R> {
        val clazz = R::class.java
        val result = arrayListOf<R>()
        list.forEach {
            val instance = clazz.newInstance()!!
            BeanUtils.copyProperties(it, instance)
            result.add(instance)
        }
        return result
    }

    /**
     * 为添加对象处理cdate和udate值,并且移除主键
     * 如果没有cdate和udate则不处理
     * TODO动态配置添加时要配置的字段列表，和是否要移除主键、时间类型
     */
    inline fun <reified R> dealInsert(obj: R): R {
        val clazz = R::class.java
        val time = (System.currentTimeMillis() / 1000).toInt()
        val id = ReflectionUtils.findField(clazz, "id")
        if (id != null) {
            ReflectionUtils.makeAccessible(id)
            id.set(obj, null)
        }
        val cdate = ReflectionUtils.findField(clazz, "cdate")
        if (cdate != null) {
            ReflectionUtils.makeAccessible(cdate)
            cdate.set(obj, time)
        }
        return dealUpdate(obj, time)
    }

    /**
     * 为更新对象处理udate值
     * 如果没有udate则不处理
     */
    inline fun <reified R> dealUpdate(obj: R, time: Int? = null): R {
        val clazz = R::class.java
        val udate = ReflectionUtils.findField(clazz, "udate")
        if (udate != null) {
            ReflectionUtils.makeAccessible(udate)
            udate.set(obj, time ?: (System.currentTimeMillis() / 1000).toInt())
        }
        return obj
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
     * 根据ids删除
     */
    protected fun deleteByIds(ids: List<Long>?): Int {
        return if (ids?.isNotEmpty() == true) {
            return if (ids.size == 1) {
                mapper.deleteByPrimaryKey(ids[0])
            } else {
                mapper.deleteByIds(StringUtils.join(ids, ","))
            }
        } else {
            0
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
        val to = to<R>(result as List<Any>)
        return PageInfo(to)
    }

    /**
     * 分页查询，返回指定的类型集合，自动从request里获取分页信息
     * 根据指定条件
     */
    inline fun <reified R> selectPage(example: Example): PageInfo<R> {
        startPage()
        val result = mapper.selectByExample(example)!!
        return getPageInfo(result as List<Any>)
    }

    /**
     * 开始分页
     */
    fun startPage() {
        val page = BaseVo().setInfo()
        PageHelper.startPage<Any>(page.pageNum, page.pageSize)
    }

    /**
     * 传入查询结果返回pageInfo对象
     */
    inline fun <reified R> getPageInfo(result: List<Any>): PageInfo<R> {
        val to = to<R>(result)
        val pageList = result as Page<R>
        pageList.clear()
        pageList.addAll(to)
        return PageInfo(pageList)
    }
}
