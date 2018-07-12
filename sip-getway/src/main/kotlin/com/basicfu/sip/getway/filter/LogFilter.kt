package com.basicfu.sip.getway.filter

import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import javax.servlet.ServletInputStream

/**
 * @author basicfu
 * @date 2018/7/12
 */
class LogFilter : ZuulFilter() {
    private val log = LoggerFactory.getLogger(LogFilter::class.java)
    override fun filterType(): String {
        return "post"
    }

    override fun filterOrder(): Int {
        return 1
    }

    override fun shouldFilter(): Boolean {
        return true
    }

    override fun run(): Any? {
        val ctx = RequestContext.getCurrentContext()
        val request = ctx.request
        var bodyStr: String? = null
        var respBody: String? = null
        var httpCode = -1
        if (isFormOrJson(request.getHeader("Content-Type"))) {
            request.inputStream.let { bodyStr = getBodyString(request.inputStream) }
        }
        ctx.response.let {
            httpCode = it.status
            if (isFormOrJson(it.getHeader("Content-Type"))) {
                respBody = ctx.responseBody
            }
        }
        log.info(
            String.format(
                "%s  %s , reqBody %s, QueryString %s httpCode %d respBody %s",
                request.method, request.requestURL, bodyStr, request.queryString, httpCode, respBody
            )
        )
        return null
    }

    private fun isFormOrJson(contentType: String?): Boolean {
        return if (contentType == null || "" == contentType) {
            false
        } else contentType.startsWith("application/x-www-form-urlencoded") || contentType.startsWith("application/json")
    }

    private fun getBodyString(inputStream: ServletInputStream): String {
        val br = BufferedReader(InputStreamReader(inputStream))
        var inputLine = br.readLine()
        var str = ""
        try {
            while (inputLine != null) {
                str += inputLine
                inputLine = br.readLine()
            }
            br.close()
        } catch (e: IOException) {
            log.error("get body error.")
        }
        return str
    }
}
