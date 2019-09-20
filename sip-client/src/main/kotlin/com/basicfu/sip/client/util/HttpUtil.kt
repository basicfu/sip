package com.basicfu.sip.client.util

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.basicfu.sip.client.common.Constant
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

    fun get(url: String, params: String? = null, app: String): String {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = 60000
        connection.addRequestProperty("app", app)
        connection.addRequestProperty(Constant.AUTHORIZATION, RequestUtil.getHeader(Constant.AUTHORIZATION))
        return inputStreamToString(connection.inputStream)
    }

    fun post(url: String, json: Any, app: String): String {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.connectTimeout = 60000
        connection.addRequestProperty("app", app)
        connection.addRequestProperty("Content-Type", "application/json")
        connection.addRequestProperty(Constant.AUTHORIZATION, RequestUtil.getHeader(Constant.AUTHORIZATION)?:"")
        connection.doOutput = true
        connection.doInput = true
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
