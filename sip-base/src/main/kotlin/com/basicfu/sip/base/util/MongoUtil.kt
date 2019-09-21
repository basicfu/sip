package com.basicfu.sip.base.util

import com.mongodb.ConnectionString
import com.mongodb.MongoClient
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.bson.Document
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.bson.codecs.pojo.PojoCodecProvider

/**
 * @author basicfu
 * @date 2019-07-05
 */
object MongoUtil {
    var database: MongoDatabase
    var collection: MongoCollection<Document>

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
