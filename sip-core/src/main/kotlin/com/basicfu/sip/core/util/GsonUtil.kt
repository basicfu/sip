package com.basicfu.sip.core.util

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject

object GsonUtil {
    private var gson = Gson()

    fun toJson(obj: Any): String {
        return gson.toJson(obj)
    }

    fun toJsonTree(obj: Any): JsonElement {
        return gson.toJsonTree(obj)
    }
    fun toJsonObject(obj: Any): JsonObject {
        return gson.toJsonTree(obj).asJsonObject
    }

    fun <T> fromJson(json: String, type: Class<T>): T {
        return gson.fromJson(json, type)
    }

//    companion object {

//        /**
//         * 自定义TypeAdapter ,null对象将被解析成空字符串
//         */
//        private val STRING = object : TypeAdapter<String>() {
//            override fun read(reader: JsonReader): String {
//                try {
//                    if (reader.peek() == JsonToken.NULL) {
//                        reader.nextNull()
//                        return ""//原先是返回Null，这里改为返回空字符串
//                    }
//                    return reader.nextString()
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//                return ""
//            }
//            override fun write(writer: JsonWriter, value: String?) {
//                try {
//                    if (value == null) {
//                        writer.nullValue()
//                        return
//                    }
//                    writer.value(value)
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//
//            }
//        }

//        /**
//         * 自定义adapter，解决由于数据类型为Int,实际传过来的值为Float，导致解析出错的问题
//         * 目前的解决方案为将所有Int类型当成Double解析，再强制转换为Int
//         */
//        private val INTEGER = object : TypeAdapter<Number>() {
//            @Throws(IOException::class)
//            override fun read(`in`: JsonReader): Number {
//                if (`in`.peek() == JsonToken.NULL) {
//                    `in`.nextNull()
//                    return 0
//                }
//                try {
//                    val i = `in`.nextDouble()
//                    return i.toInt()
//                } catch (e: NumberFormatException) {
//                    throw JsonSyntaxException(e)
//                }
//            }
//            override fun write(out: JsonWriter, value: Number) {
//                out.value(value)
//            }
//        }

//        init {
//            val gsonBulder = GsonBuilder()
//            gsonBulder.registerTypeAdapter(String::class.java, STRING)   //所有String类型null替换为字符串“”
//            gsonBulder.registerTypeAdapter(Int::class.javaPrimitiveType, INTEGER) //int类型对float做兼容
//            //通过反射获取instanceCreators属性
//            try {
//                val builder = gsonBulder.javaClass as Class<*>
//                val f = builder.getDeclaredField("instanceCreators")
//                f.isAccessible = true
//                val `val` = f.get(gsonBulder) as Map<Type, InstanceCreator<*>>//得到此属性的值
//                //注册数组的处理器
//                gsonBulder.registerTypeAdapterFactory(CollectionTypeAdapterFactory(ConstructorConstructor(`val`)))
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
}
