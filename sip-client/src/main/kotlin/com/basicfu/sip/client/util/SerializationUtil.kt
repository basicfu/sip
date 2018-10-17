package com.basicfu.sip.client.util

import io.protostuff.LinkedBuffer
import io.protostuff.ProtostuffIOUtil
import io.protostuff.Schema
import io.protostuff.runtime.RuntimeSchema
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.collections.HashMap

@Suppress("UNCHECKED_CAST")
object SerializationUtil {
    private val cachedSchema = ConcurrentHashMap<Class<*>, Schema<*>>()
    private val wrapperClass = SerializationUtil.SerializeWrapper::class.java
    val wrapperSchema = RuntimeSchema.createFrom(wrapperClass)!!
    val wrapperList = HashSet<Class<*>>()

    /**
     * 需要使用包装类型序列化
     */
    init {
        wrapperList.add(List::class.java)
        wrapperList.add(ArrayList::class.java)
        wrapperList.add(CopyOnWriteArrayList::class.java)
        wrapperList.add(LinkedList::class.java)
        wrapperList.add(Stack::class.java)
        wrapperList.add(Vector::class.java)
        wrapperList.add(Map::class.java)
        wrapperList.add(HashMap::class.java)
        wrapperList.add(TreeMap::class.java)
        wrapperList.add(Hashtable::class.java)
        wrapperList.add(SortedMap::class.java)
    }

    fun <T> getSchema(clazz: Class<T>): Schema<T> {
        var schema: Schema<T>? = cachedSchema[clazz] as? Schema<T>
        if (schema == null) {
            schema = RuntimeSchema.getSchema(clazz)
            cachedSchema[clazz] = schema
        }
        return schema!!
    }

    /**
     * 序列化
     * 会出现bean中有空list时反序列化时为null
     */
    fun <T : Any> serialize(obj: T?): Any? {
        if (obj == null) {
            return null
        }
        when (obj) {
            is Long -> return obj
            is Int -> return obj
            is String -> return obj
            is Boolean -> return obj
        }
        val clazz = obj.javaClass
        val buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE)
        try {
            var serializeObject: Any = obj
            var schema: Schema<*> = wrapperSchema
            if (!wrapperList.contains(clazz)) {
                schema = getSchema(clazz)
            } else {
                serializeObject = SerializeWrapper.build(obj)
            }
            return ProtostuffIOUtil.toByteArray<Any>(serializeObject, schema as Schema<Any>?, buffer)
        } finally {
            buffer.clear()
        }
    }

    /**
     * 反序列化,只序列ByteArray,非该类型直接返回
     * 调用方式：SerializationUtil.deserialize<Result>(byte)
     */
    inline fun <reified T> deserialize(data: Any?): T? {
        if (data == null) {
            return null
        }
        if (data !is ByteArray) {
            return data as T
        }
        val clazz = T::class.java
        return if (!wrapperList.contains(clazz)) {
            val message = clazz.newInstance()
            val schema = getSchema(clazz)
            ProtostuffIOUtil.mergeFrom(data, message, schema)
            message
        } else {
            val wrapper = SerializeWrapper<T>()
            ProtostuffIOUtil.mergeFrom(data, wrapper, wrapperSchema)
            wrapper.data!!
        }
    }

    class SerializeWrapper<T> {
        var data: T? = null

        companion object {
            fun <T> build(data: T): SerializeWrapper<T> {
                val wrapper = SerializeWrapper<T>()
                wrapper.data = data
                return wrapper
            }
        }
    }
}
