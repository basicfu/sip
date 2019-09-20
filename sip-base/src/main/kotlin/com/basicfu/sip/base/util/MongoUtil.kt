package com.basicfu.sip.base.util

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.basicfu.sip.base.model.po.User
import com.basicfu.sip.base.util.MongoUtil.collection
import com.basicfu.sip.core.common.mapper.generate
import com.mongodb.ConnectionString
import com.mongodb.MongoClient
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.eq
import io.protostuff.LinkedBuffer.use
import org.bson.Document
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.bson.codecs.pojo.PojoCodecProvider
import org.bson.conversions.Bson
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder


/**
 * @author basicfu
 * @date 2019-07-05
 */
object MongoUtil {
    lateinit var database: MongoDatabase
    lateinit var collection: MongoCollection<Document>

    init {
        val pojoCodecRegistry = fromRegistries(
            MongoClient.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build())
        )
        val settings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString("mongodb://root:Czkj1234Root@dds-wz9cbf32df43d1f4-pub.mongodb.rds.aliyuncs.com:3717/admin"))
            .codecRegistry(pojoCodecRegistry)
            .build()
        val mongoClient = MongoClients.create(settings)
        database = mongoClient.getDatabase("sip-dev")
        collection = database.getCollection("user")
    }
}

fun main() {

    val user= generate<User> {
        username="basicfu"
        nickname=""
        mobile=""
        email=""
        password= BCryptPasswordEncoder().encode("123456")
        mobileVerified=false
        emailVerified=false
        createTime=System.currentTimeMillis()
        updateTime=createTime
        blocked=false
        isdel=false
    }
    val l=MongoUtil.collection.countDocuments(eq("username",user.username))
    println(l)
    MongoUtil.collection.insertOne(Document.parse(JSON.toJSONString(user)))
//    val json = JSONObject()
//    json["a"] = "1"
//    json["b"] = "hello"
//    val doc = Document.parse(json.toJSONString())
//    collection.insertOne(doc)
//
//    println(collection.countDocuments())
//    val myDoc = collection.find().first()
//    println(myDoc?.toJson())
//
//    val cursor = collection.find().iterator()
//    cursor.use { it ->
//        while (it.hasNext()) {
//            println(it.next().toJson())
//        }
//    }
//    collection.createIndex(Document("i", 1))
}
