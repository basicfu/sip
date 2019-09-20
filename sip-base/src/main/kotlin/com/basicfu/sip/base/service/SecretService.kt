package com.basicfu.sip.base.service

import com.basicfu.sip.base.mapper.AppSecretMapper
import com.basicfu.sip.base.model.dto.SecretDto
import com.basicfu.sip.base.model.po.Secret
import com.basicfu.sip.core.common.mapper.generate
import com.basicfu.sip.core.service.BaseService
import org.springframework.stereotype.Service
import java.util.*

/**
 * @author basicfu
 * @date 2018/9/5
 */
@Service
class SecretService : BaseService<AppSecretMapper, Secret>() {

    fun all(): List<SecretDto> {
        return to(mapper.selectAll().sortedBy { it.cdate })
    }

    fun insert(): Int {
        val po = dealInsert(generate<Secret> {
            key = UUID.randomUUID().toString().replace("-", "")
            secret = UUID.randomUUID().toString().replace("-", "")
        })
        return mapper.insertSelective(po)
    }

    fun delete(ids: List<Long>?): Int {
        return deleteByIds(ids)
    }
}
