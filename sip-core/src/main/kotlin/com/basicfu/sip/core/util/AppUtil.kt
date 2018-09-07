package com.basicfu.sip.core.util

import com.basicfu.sip.core.common.Constant

/**
 * @author basicfu
 * @date 2018/9/7
 */
object AppUtil{
    fun appNotCheck(){
        ThreadLocalUtil[Constant.System.APP_SKIP] = true
    }
    fun releaseAppNotCheck(){
        ThreadLocalUtil.remove(Constant.System.APP_SKIP)
    }
}
