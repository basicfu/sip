package com.basicfu.sip.core.util

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.basicfu.sip.core.common.Constant
import com.basicfu.sip.core.common.wrapper.RequestWrapper
import com.basicfu.sip.core.model.dto.AppDto
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

/**
 * @author basicfu
 * @date 2018/9/7
 */
object AppUtil {

    /**
     * 经过网关后才有值
     */
    fun getAppId(): Long? {
        val parameter = RequestUtil.getParameter(Constant.System.APP_CODE)
        if (parameter != null) {
            return try {
                val json = JSON.parseObject(parameter)
                json.getLong(Constant.System.APP_ID)
            } catch (e: Exception) {
                null
            }
        }
        return null
    }

    /**
     * 经过网关后才有值
     */
    fun getAppCode(): String? {
        val parameter = RequestUtil.getParameter(Constant.System.APP_CODE)
        if (parameter != null) {
            return try {
                val json = JSON.parseObject(parameter)
                json.getString(Constant.System.APP_CODE)
            } catch (e: Exception) {
                null
            }
        }
        return null
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

    /**
     * 更新request中的app参数
     */
    fun updateApp(app: JSONObject) {
        val requestWrapper = RequestWrapper(RequestUtil.getRequest())
        requestWrapper.addParameter(Constant.System.APP_CODE, app.toJSONString())
        RequestContextHolder.setRequestAttributes(ServletRequestAttributes(requestWrapper))
    }

    /**
     * 根据appId获取appCode
     */
    fun getAppCodeByAppId(appId: Long): String? {
        val apps = RedisUtil.hGetAll<AppDto>(Constant.Redis.APP).map { it.value!! }.associateBy({ it.id }, { it.code })
        return apps[appId]
    }

    /**
     * 根据appCode获取appId
     */
    fun getAppIdByAppCode(appCode: String): Long? {
        val app = RedisUtil.hGet<AppDto>(Constant.Redis.APP, appCode)
        app?.let {
            return it.id
        }
        return null
    }
}
