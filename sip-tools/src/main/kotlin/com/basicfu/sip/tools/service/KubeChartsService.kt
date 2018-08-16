package com.basicfu.sip.tools.service

import com.alibaba.fastjson.JSONObject
import com.basicfu.sip.core.common.exception.CustomException
import com.basicfu.sip.core.common.mapper.example
import com.basicfu.sip.core.common.mapper.generate
import com.basicfu.sip.core.service.BaseService
import com.basicfu.sip.tools.common.Enum
import com.basicfu.sip.tools.mapper.KubeChartsMapper
import com.basicfu.sip.tools.model.po.KubeCharts
import com.basicfu.sip.tools.model.vo.KubeChartsVo
import com.basicfu.sip.tools.util.KubeUtil
import org.springframework.stereotype.Service
import java.util.*

/**
 * @author basicfu
 * @date 2018/8/16
 */
@Service
class KubeChartsService : BaseService<KubeChartsMapper, KubeCharts>() {

    fun install(name: String, set: Array<String>?): JSONObject {
        val kubeChart = mapper.selectOneByExample(example<KubeCharts> {
            andEqualTo(KubeCharts::name, name)
        })
        val result = JSONObject()
        result["rid"] = UUID.randomUUID()
        if (kubeChart == null) {
            result["success"] = false
            result["reason"] = "not found chart name is $name"
        } else {
            val exists = KubeUtil.exists(name)
            println("chart exists:$exists")
            result["success"] = true
            if (!exists) {
                KubeUtil.install(name, kubeChart.namespace!!, kubeChart.values!!, set)
                result["reason"] = "$name install success"
            } else {
                KubeUtil.update(name,kubeChart.values!!,set)
                result["reason"] = "$name update success"
            }
        }
        return result
    }

    fun insert(vo: KubeChartsVo): Int {
        if (mapper.selectCount(generate {
                name = vo.name
            }) != 0) throw CustomException(Enum.KubeCharts.EXIST_NAME)
        val po = dealInsert(to<KubeCharts>(vo))
        return mapper.insertSelective(po)
    }

}
