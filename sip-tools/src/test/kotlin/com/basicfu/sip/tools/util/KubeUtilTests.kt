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
class KubeUtilTests {
    @Autowired
    lateinit var kubeChartsService: KubeChartsService

    @Test
    fun insert() {
        val vo=generate<KubeChartsVo> {
            name="test"
            namespace="dev"
            values="prefix: test\n" +
                    "suffix: dev\n" +
                    "web:\n" +
                    "  image: registry-vpc.cn-beijing.aliyuncs.com/dmka/wutong-web\n" +
                    "  tag: latest\n" +
                    "  version: latest\n" +
                    "  count: 1\n" +
                    "  pullSecret: aliyun\n" +
                    "  port:\n" +
                    "    - 80:80\n" +
                    "  env:\n" +
                    "    API_HOST: wutong.api-dev.dmka.cn\n" +
                    "  host:\n" +
                    "    80: test.dmka.cn"
        }
        kubeChartsService.insert(vo)
    }
    @Test
    fun test1() {
//        KubeUtil.upda()
    }
    @Test
    fun delete() {
//        KubeUtil.deleteChart()
    }
    @Test
    fun exists() {
        KubeUtil.exists("test")
    }
}
