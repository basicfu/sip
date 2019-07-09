package com.basicfu.sip.base.service

import com.basicfu.sip.base.mapper.AppSecretMapper
import com.basicfu.sip.base.model.po.AppSecret
import com.basicfu.sip.base.model.vo.AppSecretVo
import com.basicfu.sip.base.model.dto.AppSecretDto
import com.basicfu.sip.core.common.mapper.example
import com.basicfu.sip.core.common.mapper.generate
import com.basicfu.sip.core.service.BaseService
import com.github.pagehelper.PageInfo
import org.springframework.stereotype.Service
import java.util.*

/**
 * @author basicfu
 * @date 2018/9/5
 */
@Service
class AppSecretService : BaseService<AppSecretMapper, AppSecret>() {

    fun list(vo: AppSecretVo): PageInfo<AppSecretDto> {
        return selectPage(example<AppSecret> {
            orLike {
                secret = vo.q
                description = vo.q
            }
            orderByDesc(AppSecret::cdate)
        })
    }

    fun all(): List<AppSecretDto> {
        return to(mapper.selectAll().sortedBy { it.cdate })
    }

    fun insert(vo: AppSecretVo): Int {
        val po = dealInsert(generate<AppSecret> {
            secret = UUID.randomUUID().toString().replace("-", "")
            description = vo.description
        })
        return mapper.insertSelective(po)
    }

    fun update(vo: AppSecretVo): Int {
        val po = dealUpdate(generate<AppSecret> {
            id = vo.id
            description = vo.description
        })
        return mapper.updateByPrimaryKeySelective(po)
    }

    fun delete(ids: List<Long>?): Int {
        return deleteByIds(ids)
    }
}
