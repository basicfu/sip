package com.basicfu.sip.base.service

import com.basicfu.sip.base.mapper.AppServiceMapper
import com.basicfu.sip.base.model.po.AppService
import com.basicfu.sip.base.model.vo.AppServiceVo
import com.basicfu.sip.common.constant.Constant
import com.basicfu.sip.common.enum.Enum
import com.basicfu.sip.common.model.dto.AppServiceDto
import com.basicfu.sip.common.util.AppUtil
import com.basicfu.sip.core.common.exception.CustomException
import com.basicfu.sip.core.common.mapper.example
import com.basicfu.sip.core.common.mapper.generate
import com.basicfu.sip.core.service.BaseService
import com.github.pagehelper.PageInfo
import org.springframework.stereotype.Service

/**
 * @author basicfu
 * @date 2018/6/22
 */
@Service
class AppServiceService : BaseService<AppServiceMapper, AppService>() {

    fun list(vo: AppServiceVo): PageInfo<AppServiceDto> {
        return selectPage(example<AppService> {
            orLike {
                name = vo.q
                serverId = vo.q
            }
            orderByDesc(AppService::cdate)
        })
    }

    /**
     * 0只查询自己的service
     * 1查询自己和sip的service
     */
    fun all(type:Int=0): List<AppServiceDto> {
        return if(type==0){
            to(mapper.selectAll().sortedBy { it.cdate })
        }else{
            val appIds= arrayListOf(AppUtil.getAppId())
            val appCode = AppUtil.getAppCode()
            if(Constant.System.APP_SYSTEM_CODE!=appCode){
                appIds.add(AppUtil.getAppIdByAppCode(Constant.System.APP_SYSTEM_CODE))
            }
            AppUtil.notCheckApp()
            to(mapper.selectByExample(example<AppService> {
                andIn(AppService::appId,appIds)
            }))
        }
    }

    fun insert(vo: AppServiceVo): Int {
        if (vo.serverId.isNullOrBlank() && vo.url.isNullOrBlank()) {
            throw CustomException(Enum.NOT_NULL_SERVER_ID_OR_URL)
        }
        if (mapper.selectCount(generate {
                path = vo.path
            }) != 0) throw CustomException(Enum.EXIST_APP_SERVICE_PATH)
        if (!vo.serverId.isNullOrBlank() && mapper.selectCount(generate {
                serverId = vo.serverId
            }) != 0) throw CustomException(Enum.EXIST_APP_SERVER_ID)
        if (!vo.url.isNullOrBlank() && mapper.selectCount(generate {
                url = vo.url
            }) != 0) throw CustomException(Enum.EXIST_APP_SERVER_URL)
        val po = dealInsert(to<AppService>(vo))
        return mapper.insertSelective(po)
    }

    fun update(vo: AppServiceVo): Int {
        if (vo.serverId.isNullOrBlank() && vo.url.isNullOrBlank()) {
            throw CustomException(Enum.NOT_NULL_SERVER_ID_OR_URL)
        }
        val checkPath = mapper.selectOne(generate {
            path = vo.path
        })
        if (checkPath != null && checkPath.id != vo.id) throw CustomException(Enum.EXIST_APP_SERVICE_PATH)
        if (!vo.serverId.isNullOrBlank()) {
            val checkServiceId = mapper.selectOne(generate {
                serverId = vo.serverId
            })
            if (checkServiceId != null && checkServiceId.id != vo.id) throw CustomException(
                Enum.EXIST_APP_SERVER_ID
            )
        }
        if (!vo.url.isNullOrBlank()) {
            val checkUrl = mapper.selectOne(generate {
                url = vo.url
            })
            if (checkUrl != null && checkUrl.id != vo.id) throw CustomException(
                Enum.EXIST_APP_SERVER_URL
            )
        }
        val po = dealUpdate(to<AppService>(vo))
        return mapper.updateByPrimaryKeySelective(po)
    }

    fun delete(ids: List<Long>?): Int {
        return deleteByIds(ids)
    }
}
