package com.basicfu.sip.base.util

import com.basicfu.sip.base.common.constant.Constant
import com.basicfu.sip.base.model.dto.AppDto
import com.basicfu.sip.core.common.wrapper.RequestWrapper
import com.basicfu.sip.core.util.RedisUtil
import com.basicfu.sip.core.util.RequestUtil
import com.basicfu.sip.core.util.ThreadLocalUtil
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
        return RequestUtil.getParameter(Constant.System.APP_ID)?.toLong()
    }

    /**
     * 经过网关后才有值
     */
    fun getAppCode(): String? {
        return RequestUtil.getParameter(Constant.System.APP_CODE)
    }

    /**
     * 获取当前应用id和系统应用id
     */
    fun getAppIdAndSipId(): List<Long> {
        val appIds= arrayListOf(AppUtil.getAppId()!!)
        val appCode = AppUtil.getAppCode()
        if(Constant.System.APP_SYSTEM_CODE!=appCode){
            appIds.add(AppUtil.getAppIdByAppCode(Constant.System.APP_SYSTEM_CODE)!!)
        }
        return appIds
    }
    /**
     * 不检查app
     * forever默认为false一次有效，下次sql不在检查，true为当前线程中永久不在检查app，除非主动释放
     */
    fun notCheckApp(count:Int=1){
        ThreadLocalUtil[Constant.System.NOT_CHECK_APP] = count
    }
    /**
     * 继续检查app
     */
    fun releaseNotCheckApp() {
        ThreadLocalUtil.remove(Constant.System.NOT_CHECK_APP)
    }

    /**
     * 更新request中的app参数
     */
    fun updateApp(appId: Long?,appCode:String?) {
        val requestWrapper = RequestWrapper(RequestUtil.getRequest())
        requestWrapper.addParameter(Constant.System.APP_ID, appId)
        requestWrapper.addParameter(Constant.System.APP_CODE, appCode)
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
