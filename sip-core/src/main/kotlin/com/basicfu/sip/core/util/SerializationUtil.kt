package com.basicfu.sip.core.util

import io.protostuff.*
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

//    fun <T:Any> serialize(any: Any?): ByteArray? {
//        if (any == null) {
//            return null
//        }
//        return s(any as ByteArray)
//    }
//    inline fun <reified T> deserialize(any: Any?): T? {
//        if (any == null) {
//            return null
//        }
//        return deserialize(any as ByteArray)
//    }

    /**
     * 序列化
     */
    fun <T : Any> serialize(obj: T?): ByteArray? {
        if(obj==null){
            return null
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
     * 反序列化
     * 调用方式：SerializationUtil.deserialize<Result>(byte)
     */
    inline fun <reified T> deserialize(data: Any?): T? {
        if(data==null){
            return null
        }
        data as ByteArray
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

    //    inline fun <reified T> serialize(map: Map<String, T>): ByteArray {
//        val clazz = T::class.java
//        val buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE)
//        val schema =MessageMapSchema(RuntimeSchema.getSchema(String::class.java), RuntimeSchema.getSchema(clazz))
//        return ProtostuffIOUtil.toByteArray(map, schema, buffer)
//    }

//    inline fun <reified T> deserializeMap(data: ByteArray): Map<String,T> {
//        val clazz = T::class.java
//        val buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE)
//        val schema =MessageMapSchema(RuntimeSchema.getSchema(String::class.java), RuntimeSchema.getSchema(clazz))
//        return ProtostuffIOUtil.toByteArray(map, schema, buffer)
//    }


    //    val newHash = hashMapOf<String, Long>()
//    ProtostuffIOUtil.mergeFrom(data, newHash, messageMapSchema)
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