package com.basicfu.sip.client.util

import com.alibaba.fastjson.JSON
import java.io.BufferedWriter
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL


/**
 * @author basicfu
 * @date 2019-07-04
 */

object HttpUtil {

    val host="http://localhost:7100"
    fun get(url:String,params:String): String {
        val connection = URL(host+url).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = 60000
        return inputStreamToString(connection.inputStream)
    }
    fun post(url:String,json:Any):String{
        val connection = URL(host+url).openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.connectTimeout = 60000
        val out = BufferedWriter(OutputStreamWriter(connection.outputStream))
        out.write(JSON.toJSONString(json))
        out.flush()
        return inputStreamToString(connection.inputStream)

    }
    private fun inputStreamToString(inputStream: InputStream): String {
        val result = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var length = inputStream.read(buffer)
        while (length != -1) {
            result.write(buffer, 0, length)
            length = inputStream.read(buffer)
        }
        return result.toString("UTF-8")
    }
}
