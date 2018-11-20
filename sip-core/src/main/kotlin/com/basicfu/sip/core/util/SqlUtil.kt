package com.basicfu.sip.core.util

/**
 * @author basicfu
 * @date 2018/8/29
 */
object SqlUtil {
    /**
     * 已添加''
     */
    fun dealLikeValue(v: String?): String? {
        if (v == null) {
            return null
        }
        var value = v.replace("'", "\\'")
        if (value.contains("%") || value.contains("_")) {
            value = value.replace("%", "\\%").replace("_", "\\_")
        }
        return "'%$value%'"
    }
}
