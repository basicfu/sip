package com.basicfu.sip.tools.service

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.basicfu.sip.core.common.exception.CustomException
import com.basicfu.sip.core.common.mapper.example
import com.basicfu.sip.core.common.mapper.generate
import com.basicfu.sip.core.service.BaseService
import com.basicfu.sip.tools.common.Enum
import com.basicfu.sip.tools.mapper.KubeChartsMapper
import com.basicfu.sip.tools.model.po.KubeCharts
import com.basicfu.sip.tools.model.vo.KubeChartsVo
import com.basicfu.sip.tools.util.HelmUtil
import org.apache.commons.lang.StringUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * @author basicfu
 * @date 2018/8/16
 */
@Service
class KubeChartsService : BaseService<KubeChartsMapper, KubeCharts>() {

    fun install(name: String, set: Array<String>?): String {
        val kubeChart = mapper.selectOneByExample(example<KubeCharts> {
            andEqualTo(KubeCharts::name, name)
        })
        val result=LinkedList<String>()
        result.add("rid:${UUID.randomUUID()}")
        if (kubeChart == null) {
            result.add("success:false")
            result.add("message:not found chart name is $name")
        } else {
            val exists = HelmUtil.exists(name)
            result.add("success:true")
            var jsonParam=JSONObject()
            try {
                jsonParam=JSON.parseObject(kubeChart.param)
            }catch (e:Exception){
            }
            set?.forEach {
                val split=it.split("=")
                jsonParam[split[0]]=split[1]
            }
            var param=""
            jsonParam.keys.forEach {
                param="--set $it=${jsonParam[it]} "
            }
            result.add("param:$param")
            if (exists) {
                result.add("message:$name update success")
                result.add("desc:"+HelmUtil.update(name,kubeChart.values!!,param))
            } else {
                result.add("message:$name install success")
                result.add("desc:"+HelmUtil.install(name, kubeChart.namespace!!, kubeChart.values!!, param))
            }
            mapper.updateByExampleSelective(generate<KubeCharts> {
                this.param=jsonParam.toJSONString()
            },example<KubeCharts> {
                andEqualTo(KubeCharts::name,name)
            })
        }
        return StringUtils.join(result,"\n")+"\n"
    }

    fun insert(vo: KubeChartsVo): Int {
        if (mapper.selectCount(generate {
                name = vo.name
            }) != 0) throw CustomException(Enum.KubeCharts.EXIST_NAME)
        val po = dealInsert(to<KubeCharts>(vo))
        return mapper.insertSelective(po)
    }

}
