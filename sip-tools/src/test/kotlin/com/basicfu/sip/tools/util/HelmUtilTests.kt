package com.basicfu.sip.tools.util

import com.basicfu.sip.core.common.mapper.generate
import com.basicfu.sip.tools.model.vo.KubeChartsVo
import com.basicfu.sip.tools.service.KubeChartsService
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

/**
 * @author basicfu
 * @date 2018/8/16
 */
@RunWith(SpringRunner::class)
@SpringBootTest
class HelmUtilTests {
    @Autowired
    lateinit var kubeChartsService: KubeChartsService

    @Test
    fun insert() {
        val vo=generate<KubeChartsVo> {
            name="gitlab"
            namespace="prod"
            values=""
        }
        kubeChartsService.insert(vo)
    }
    @Test
    fun install() {
        val install = kubeChartsService.install("test", arrayOf("web.tag=latest"))
        println(install)
    }
}
