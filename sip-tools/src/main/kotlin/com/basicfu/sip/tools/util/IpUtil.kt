package com.basicfu.sip.tools.util


import java.util.regex.Pattern
import javax.servlet.http.HttpServletRequest

/**
 * @author basicfu
 * @date 2018/8/15
 */
object IpUtil {

    private const val A_255 = "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)"
    private val pattern = Pattern.compile("^(?:$A_255\\.){3}$A_255$")

    private fun ipV4ToLong(ip: String): Long {
        val octets = ip.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return ((java.lang.Long.parseLong(octets[0]) shl 24) + (Integer.parseInt(octets[1]) shl 16).toLong()
                + (Integer.parseInt(octets[2]) shl 8).toLong() + Integer.parseInt(octets[3]).toLong())
    }

    private fun isIPv4Private(ip: String): Boolean {
        val longIp = ipV4ToLong(ip)
        return (longIp >= ipV4ToLong("10.0.0.0") && longIp <= ipV4ToLong("10.255.255.255")
                || longIp >= ipV4ToLong("172.16.0.0") && longIp <= ipV4ToLong("172.31.255.255")
                || longIp >= ipV4ToLong("192.168.0.0") && longIp <= ipV4ToLong("192.168.255.255"))
    }

    private fun isIPv4Valid(ip: String): Boolean {
        return pattern.matcher(ip).matches()
    }

    fun getIpAddress(request: HttpServletRequest): String {
        var ip = request.getHeader("x-forwarded-for")
        var found = false
        if (ip != null) {
            val ips = ip.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (i in ips) {
                ip = i
                if (isIPv4Valid(ip) && !isIPv4Private(ip)) {
                    found = true
                    break
                }
            }
        }
        if (!found) {
            ip = request.remoteAddr
        }
        return ip
    }

}
