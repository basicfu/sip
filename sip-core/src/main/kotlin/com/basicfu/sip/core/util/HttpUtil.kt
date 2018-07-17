package com.basicfu.sip.core.util

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializerFeature
import org.apache.http.Consts
import org.apache.http.Header
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.entity.mime.HttpMultipartMode
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.entity.mime.content.FileBody
import org.apache.http.entity.mime.content.StringBody
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset


/**
 * @author fuliang
 * *
 * @date 4/21/2017
 */
object HttpUtil {
//    val cookie=response.getHeaders("Set-Cookie")
//    cookie.forEach { e->
//        println(e.name+"-"+e.value)
//    }

    /**针对某url获取302地址*/
    fun getRealUrl(url: String): String? {
        val httpClient = HttpClients.createDefault()
        val nurl = url.replace(" ", "%20")
        val httpGet = HttpGet(nurl)
        val requestConfig = RequestConfig.custom().setConnectTimeout(15000).setConnectionRequestTimeout(10000).setSocketTimeout(60000).setRedirectsEnabled(false).build()
        httpGet.config = requestConfig
        val response: CloseableHttpResponse
        try {
            response = httpClient.execute(httpGet)
            if (response.statusLine.statusCode == 302) {
                val headers = response.getHeaders("Location")
                if (headers.isNotEmpty()) {
                    return headers[0].value
                }
            }
            return null
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun getLocation(url: String, headers: Map<String, String>? = null, randomUa: Boolean = true): String? {
        return get(url, headers, randomUa, false, true)?.headers?.filter { it.name == "Location" }?.get(0)?.value
    }

    fun getCookies(url: String, headers: Map<String, String>? = null, randomUa: Boolean = true): List<String>? {
        return get(url, headers, randomUa, false, true)?.headers?.filter { it.name.toLowerCase() == "set-cookie" }?.map { it.value }
    }

    fun getHeaders(url: String, headers: Map<String, String>? = null, randomUa: Boolean = true): Array<Header>? {
        return get(url, headers, randomUa, false, true)?.headers
    }

    fun get(url: String): String? {
        return get(url, null, true, true, false)?.entity
    }

    fun get(url: String, headers: Map<String, String>? = null): String? {
        return get(url, headers, true, true, false)?.entity
    }

    fun get(url: String, headers: Map<String, String>? = null, randomUa: Boolean = true): String? {
        return get(url, headers, randomUa, true, false)?.entity
    }

    fun getResponse(url: String, headers: Map<String, String>? = null, randomUa: Boolean = true): Response? {
        return get(url, headers, randomUa, true, true)
    }

    private fun get(url: String, headers: Map<String, String>?, randomUa: Boolean, needEntity: Boolean, needHeaders: Boolean): Response? {
        val httpClient = HttpClients.createDefault()
//        URLEncoder.encode(url, "utf-8")
        val httpGet = HttpGet(url)
        val response: CloseableHttpResponse
        try {
            val requestConfig = RequestConfig.custom().setConnectTimeout(15000).setConnectionRequestTimeout(10000).setSocketTimeout(60000).build()
            httpGet.config = requestConfig
            headers?.let { it.keys.forEach { e -> httpGet.setHeader(e, headers[e]) } }
            if (randomUa) httpGet.setHeader("user-agent", UA.randomUserAgent())
            val resp = Response()
            response = httpClient.execute(httpGet)
            if (needEntity) resp.entity = EntityUtils.toString(response.entity)
            if (needHeaders) resp.headers = response.allHeaders
            return resp
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun post(url: String, headers: Map<String, String>? = null, params: Map<String, String>? = null, requestEncoding: String? = "UTF-8", randomUa: Boolean = true): String? {
        return post(url, headers, params, null, requestEncoding, randomUa, true, false)?.entity
    }

    fun post(url: String, params: Map<String, String>? = null, requestEncoding: String? = "UTF-8", randomUa: Boolean = true): String? {
        return post(url, null, params, null, requestEncoding, randomUa, true, false)?.entity
    }

    fun post(url: String, params: Map<String, String>? = null): String? {
        return post(url, null, params, null, "UTF-8", true, true, false)?.entity
    }

    fun postJson(url: String, headers: Map<String, String>? = null, json: Any? = null, requestEncoding: String? = "UTF-8", randomUa: Boolean = true): String? {
        return post(url, headers, null, json, requestEncoding, randomUa, true, false)?.entity
    }

    fun postJson(url: String, json: Any? = null, requestEncoding: String? = "UTF-8", randomUa: Boolean = true): String? {
        return post(url, null, null, json, requestEncoding, randomUa, true, false)?.entity
    }

    fun postJson(url: String, json: Any? = null): String? {
        return post(url, null, null, json, "UTF-8", true, true, false)?.entity
    }

    fun postFile() {

    }

    fun postResponse(url: String, headers: Map<String, String>? = null, params: Map<String, String>? = null, requestEncoding: String? = "UTF-8"): Response? {
        return post(url, headers, params, null, requestEncoding, true, true, true)
    }

    private fun post(url: String, headers: Map<String, String>?, params: Map<String, String>?, json: Any?, requestEncoding: String?, randomUa: Boolean, needEntity: Boolean, needHeaders: Boolean): Response? {
        val response: CloseableHttpResponse?
        val httpClient = HttpClients.createDefault()
        val httpPost = HttpPost(url)
        try {
            headers?.let { it.keys.forEach { e -> httpPost.setHeader(e, headers[e]) } }
            if (randomUa) httpPost.setHeader("user-agent", UA.randomUserAgent())
            if (params != null) {
                httpPost.entity = UrlEncodedFormEntity(params.keys.map { BasicNameValuePair(it, params[it]) }, requestEncoding
                        ?: "UTF-8")
//                httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded")
            } else if (json != null) {
                httpPost.entity = StringEntity(JSON.toJSONString(json, SerializerFeature.WriteNullStringAsEmpty), Charset.forName(requestEncoding ?: "UTF-8"))
                httpPost.setHeader("Content-Type", "application/json")
            }
            val resp = Response()
            response = httpClient.execute(httpPost)
            if (needEntity) resp.entity = EntityUtils.toString(response.entity)
            if (needHeaders) resp.headers = response.allHeaders
            return resp
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun postFile(url: String, header: Map<String, String>, postData: Map<String, Any>): String? {
        val response: CloseableHttpResponse?
        val httpClient = HttpClients.createDefault()
        val httpPost = HttpPost(url)
        try {
            header.keys.forEach { e -> httpPost.setHeader(e, header[e]) }
            val meb = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
            for (key in postData.keys) {
                val obj = postData[key]
                if (obj is File) {
                    meb.addPart(key, FileBody(obj, ContentType.create("image/jpeg", Consts.UTF_8)))
                } else if (obj is String) {
                    meb.addPart(key, StringBody(obj.toString(), ContentType.create("text/plain", Consts.UTF_8)))
                }
            }
            httpPost.entity = meb.build()
            response = httpClient!!.execute(httpPost)
            return EntityUtils.toString(response!!.entity)
        } catch (e: IOException) {
        }
        return null
    }

    class Request {
        /**请求url*/
        var url: String? = null
        /**请求头*/
        var headers: Map<Any, Any>? = null
        /**请求参数*/
        var params: Map<Any, Any>? = null
        /**请求json*/
        var json: Any? = null
        /**启用随机user-agent*/
        var randomUa = true
    }

    class Response {
        /**响应结果*/
        var entity: String? = null
        /**响应headers*/
        var headers: Array<Header>? = null
    }

    object UA {
        val list = arrayListOf(
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36",
                "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.8) Gecko Fedora/1.9.0.8-1.fc10 Kazehakase/0.5.6",
                "Mozilla/5.0 (X11; Linux i686; U;) Gecko/20070322 Kazehakase/0.4.5",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.71 Safari/537.1 LBBROWSER",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_3) AppleWebKit/535.20 (KHTML, like Gecko) Chrome/19.0.1036.7 Safari/535.20",
                "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.8) Gecko Fedora/1.9.0.8-1.fc10 Kazehakase/0.5.6",
                "Mozilla/5.0 (X11; U; Linux x86_64; zh-CN; rv:1.9.2.10) Gecko/20100922 Ubuntu/10.10 (maverick) Firefox/3.6.10",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.71 Safari/537.1 LBBROWSER",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1",
                "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.8.0.12) Gecko/20070731 Ubuntu/dapper-security Firefox/1.5.0.12",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")

        fun randomUserAgent(): String {
            return list[(Math.random() * list.size).toInt()]
        }
    }

}
//val build = HttpClients.custom()
//val proxy = HttpHost("127.0.0.1", 8812)
//val httpClient = build.setProxy(proxy).build()

//retry
//var httpclient = HttpClients.custom()
//        .setRetryHandler(HttpRequestRetryHandler { exception, executionCount, context ->
//            if (executionCount >= 5) {
//                // 如果已经重试了5次，就放弃
//                return@HttpRequestRetryHandler false
//            }
//            if (exception is InterruptedIOException) {
//                // 超时
//                return@HttpRequestRetryHandler false
//            }
//            if (exception is UnknownHostException) {
//                // 目标服务器不可达
//                return@HttpRequestRetryHandler false
//            }
//            if (exception is ConnectTimeoutException) {
//                // 连接被拒绝
//                return@HttpRequestRetryHandler false
//            }
//            if (exception is SSLException) {
//                // ssl握手异常
//                return@HttpRequestRetryHandler false
//            }
//            val clientContext = HttpClientContext.adapt(context)
//            val request = clientContext.request
//            val idempotent = request !is HttpEntityEnclosingRequest
//            idempotent
//        })
//        .build()
//
//val httpClientBuilder = HttpClientBuilder.create()
//try
//{
//    val ctx = SSLContext.getInstance("TLS")
//    val tm = object : X509TrustManager {
//        @Throws(CertificateException::class)
//        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
//        }
//
//        @Throws(CertificateException::class)
//        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
//        }
//
//        override fun getAcceptedIssuers(): Array<X509Certificate>? {
//            return null
//        }
//    }
//    ctx.init(null, arrayOf<TrustManager>(tm), null)
//    httpClientBuilder.setSSLContext(ctx)
//}
//catch (e:Exception) {
//    e.printStackTrace()
//}
//
//val httpClient = httpClientBuilder.build()
//
//val acceptingTrustStrategy = { chain: Array<X509Certificate>, authType: String -> true }
//val sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build()
//val csf = SSLConnectionSocketFactory(sslContext)
//httpClientBuilder.setSSLSocketFactory(csf)
