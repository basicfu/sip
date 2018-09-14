package com.basicfu.sip.core.util

import com.basicfu.sip.core.common.Constant

/**
 * @author basicfu
 * @date 2018/9/7
 */
object AppUtil {

    /**
     * 拦截器后app值是id
     */
    fun getAppId(): Long? {
        return RequestUtil.getParameter(Constant.System.APP_CODE)?.toLong()
    }

    /**
     * 在拦截器前app是code
     */
    fun getAppCode(): String? {
        return RequestUtil.getParameter(Constant.System.APP_CODE)
    }

    /**
     * 下一句sql不拼接appId
     */
    fun appNotCheck() {
        ThreadLocalUtil[Constant.System.APP_SKIP] = true
    }

    /**
     * 释放下一句sql继续拼接
     */
    fun releaseAppNotCheck() {
        ThreadLocalUtil.remove(Constant.System.APP_SKIP)
    }
}
