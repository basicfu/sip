package com.basicfu.sip.api.service

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializerFeature
import com.basicfu.sip.api.mapper.InterfaceHistoryMapper
import com.basicfu.sip.api.mapper.InterfaceMapper
import com.basicfu.sip.api.model.dto.InterfaceDto
import com.basicfu.sip.api.model.po.Interface
import com.basicfu.sip.api.model.po.InterfaceHistory
import com.basicfu.sip.api.model.vo.InterfaceVo
import com.basicfu.sip.common.enum.Enum
import com.basicfu.sip.common.enum.Enum.ReqBodyType.*
import com.basicfu.sip.common.enum.Enum.RequestMethod.*
import com.basicfu.sip.core.common.exception.CustomException
import com.basicfu.sip.core.common.mapper.example
import com.basicfu.sip.core.common.mapper.generate
import com.basicfu.sip.core.service.BaseService
import org.apache.commons.lang3.StringUtils
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.*
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.IOException

/**
 * @author basicfu
 * @date 2019/1/11
 */
@Service
class InterfaceService : BaseService<InterfaceMapper, Interface>() {
    @Autowired
    lateinit var interfaceHIstoryMapper: InterfaceHistoryMapper

    fun get(id: Long): InterfaceDto? {
        return to(mapper.selectByPrimaryKey(id))
    }

    fun insert(vo: InterfaceVo): Int {
        if (mapper.selectCount(generate {
                method = vo.method!!.name
                path = vo.path
            }) != 0) throw CustomException(Enum.EXIST_INTERFACE_NAME)
        val po = dealInsert(to<Interface>(vo))
        return mapper.insertSelective(po)
    }

    /**
     * 更新接口后添加保存记录
     */
    fun update(vo: InterfaceVo): Int {
        val interfacePo = mapper.selectByPrimaryKey(vo.id) ?: throw CustomException(Enum.NOT_FOUND_DATA)
        val checkMethodAndPath = mapper.selectOne(generate {
            projectId=interfacePo.projectId
            method = vo.method!!.name
            path = vo.path
        })
        if (checkMethodAndPath != null && checkMethodAndPath.id != vo.id) throw CustomException(Enum.EXIST_INTERFACE_NAME)
        //暂设每次保存都保存历史
        val history = to<InterfaceHistory>(interfacePo)!!
        history.id=null
        history.interfaceId = interfacePo.id
        history.type=Enum.InterfaceHistoryType.SAVE.name
        interfaceHIstoryMapper.insertSelective(dealInsert(history))
        val po = dealUpdate(to<Interface>(vo))!!
        return mapper.updateByPrimaryKeySelective(po)
    }

    fun delete(ids: List<Long>): Int {
        if (ids.isNotEmpty()) {
            interfaceHIstoryMapper.deleteByExample(example<InterfaceHistory> {
                andIn(InterfaceHistory::interfaceId, ids)
            })
        }
        return deleteByIds(ids)
    }

    /**
     * 运行接口
     */
    fun run(vo: InterfaceVo): Int {
        //处理参数
        var url = vo.host
        var path = vo.path
        val headers = vo.reqHeaders
        val method = vo.method!!
        val reqBodyType = vo.reqBodyType
        val reqBody = vo.reqBody
        vo.pathParams?.forEach {
            path = path!!.replaceFirst("{${it.key}}", "${it.value}")
        }
        vo.queryParams?.let { params ->
            path += "?" + StringUtils.join(params.map { it -> "${it.key}=${it.value}" }, "&")
        }
        //TODO 关于编码的选择，js encodeURI和js encodeURIComponent
        //TODO 关于delete请求是否携带请求体的疑问，转发使用，实际请求不用
//        path=URLEncoder.encode(path, Charsets.UTF_8.name())
        url += path
        //http
        val httpClient = HttpClients.createDefault()
        val httpRequest: HttpRequestBase
        val response: CloseableHttpResponse
        try {
            val requestConfig = RequestConfig.custom()
                .setConnectTimeout(15000)
                .setConnectionRequestTimeout(10000)
                .setSocketTimeout(60000)
                .build()
            httpRequest = when (method) {
                GET -> HttpGet(url)
                POST -> HttpPost(url)
                PUT -> HttpPut(url)
                PATCH -> HttpPatch(url)
                DELETE -> HttpDelete(url)
                HEAD -> HttpHead(url)
                OPTIONS -> HttpOptions(url)
            }
            headers?.forEach {
                httpRequest.setHeader(it.key, it.value)
            }
            httpRequest.config = requestConfig
            if (method in listOf(POST, PUT, PATCH)) {
                httpRequest as HttpEntityEnclosingRequestBase
                reqBodyType?.let {
                    when (it) {
                        FORM -> {
//                            httpRequest.entity = UrlEncodedFormEntity(params.keys.map { BasicNameValuePair(it, params[it]) }, Charsets.UTF_8)
                        }
                        JSON -> {
                            httpRequest.entity = StringEntity(
                                JSON.toJSONString(reqBody, SerializerFeature.WriteNullStringAsEmpty),
                                Charsets.UTF_8
                            )
                            httpRequest.setHeader("Content-Type", "application/json")
                        }
                        FILE -> TODO()
                        RAW -> TODO()
                    }
                }
            }
            response = httpClient.execute(httpRequest)
            val entity = EntityUtils.toString(response.entity)
//            val headers= response.allHeaders
            println(entity)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return 1
    }
}
