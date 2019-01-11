package com.basicfu.sip.api.model.po

import javax.persistence.*

class Interface {
    /**
     * @return id
     */
    /**
     * @param id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    /**
     * 接口名
     */
    /**
     * 获取接口名
     *
     * @return name - 接口名
     */
    /**
     * 设置接口名
     *
     * @param name 接口名
     */
    var name: String? = null

    /**
     * 接口描述
     */
    /**
     * 获取接口描述
     *
     * @return description - 接口描述
     */
    /**
     * 设置接口描述
     *
     * @param description 接口描述
     */
    var description: String? = null

    /**
     * 接口状态（完成、未完成）
     */
    /**
     * 获取接口状态（完成、未完成）
     *
     * @return status - 接口状态（完成、未完成）
     */
    /**
     * 设置接口状态（完成、未完成）
     *
     * @param status 接口状态（完成、未完成）
     */
    var status: String? = null

    /**
     * 接口分类ID
     */
    /**
     * 获取接口分类ID
     *
     * @return category_id - 接口分类ID
     */
    /**
     * 设置接口分类ID
     *
     * @param categoryId 接口分类ID
     */
    @Column(name = "category_id")
    var categoryId: Long? = null

    /**
     * 请求方式，GET/POST/PUT等
     */
    /**
     * 获取请求方式，GET/POST/PUT等
     *
     * @return method - 请求方式，GET/POST/PUT等
     */
    /**
     * 设置请求方式，GET/POST/PUT等
     *
     * @param method 请求方式，GET/POST/PUT等
     */
    var method: String? = null

    /**
     * path
     */
    /**
     * 获取path
     *
     * @return path - path
     */
    /**
     * 设置path
     *
     * @param path path
     */
    var path: String? = null

    /**
     * path参数数组key/value/desc/enable
     */
    /**
     * 获取path参数数组key/value/desc/enable
     *
     * @return path_params - path参数数组key/value/desc/enable
     */
    /**
     * 设置path参数数组key/value/desc/enable
     *
     * @param pathParams path参数数组key/value/desc/enable
     */
    @Column(name = "path_params")
    var pathParams: String? = null

    /**
     * URL请求参数数组----------跟在path之后,key/value/desc/enable
     */
    /**
     * 获取URL请求参数数组----------跟在path之后,key/value/desc/enable
     *
     * @return query_params - URL请求参数数组----------跟在path之后,key/value/desc/enable
     */
    /**
     * 设置URL请求参数数组----------跟在path之后,key/value/desc/enable
     *
     * @param queryParams URL请求参数数组----------跟在path之后,key/value/desc/enable
     */
    @Column(name = "query_params")
    var queryParams: String? = null

    /**
     * 请求头数组----------,key/value/desc/enable
     */
    /**
     * 获取请求头数组----------,key/value/desc/enable
     *
     * @return req_headers - 请求头数组----------,key/value/desc/enable
     */
    /**
     * 设置请求头数组----------,key/value/desc/enable
     *
     * @param reqHeaders 请求头数组----------,key/value/desc/enable
     */
    @Column(name = "req_headers")
    var reqHeaders: String? = null

    /**
     * [form,json,file,raw]
     */
    /**
     * 获取[form,json,file,raw]
     *
     * @return req_body_type - [form,json,file,raw]
     */
    /**
     * 设置[form,json,file,raw]
     *
     * @param reqBodyType [form,json,file,raw]
     */
    @Column(name = "req_body_type")
    var reqBodyType: String? = null

    /**
     * 请求体，form(根据是否选择file自动改变请求头类型)(key/type(text/file)/value/desc/enable),json,file(一个路径path),raw(text)
     */
    /**
     * 获取请求体，form(根据是否选择file自动改变请求头类型)(key/type(text/file)/value/desc/enable),json,file(一个路径path),raw(text)
     *
     * @return req_body - 请求体，form(根据是否选择file自动改变请求头类型)(key/type(text/file)/value/desc/enable),json,file(一个路径path),raw(text)
     */
    /**
     * 设置请求体，form(根据是否选择file自动改变请求头类型)(key/type(text/file)/value/desc/enable),json,file(一个路径path),raw(text)
     *
     * @param reqBody 请求体，form(根据是否选择file自动改变请求头类型)(key/type(text/file)/value/desc/enable),json,file(一个路径path),raw(text)
     */
    @Column(name = "req_body")
    var reqBody: String? = null

    /**
     * [json,file,raw]
     */
    /**
     * 获取[json,file,raw]
     *
     * @return res_body_type - [json,file,raw]
     */
    /**
     * 设置[json,file,raw]
     *
     * @param resBodyType [json,file,raw]
     */
    @Column(name = "res_body_type")
    var resBodyType: String? = null

    /**
     * 请求体，form(根据是否选择file自动改变请求头类型)(key/type(text/file)/value/desc/enable),json,file(一个路径path),raw(text)
     */
    /**
     * 获取请求体，form(根据是否选择file自动改变请求头类型)(key/type(text/file)/value/desc/enable),json,file(一个路径path),raw(text)
     *
     * @return res_body - 请求体，form(根据是否选择file自动改变请求头类型)(key/type(text/file)/value/desc/enable),json,file(一个路径path),raw(text)
     */
    /**
     * 设置请求体，form(根据是否选择file自动改变请求头类型)(key/type(text/file)/value/desc/enable),json,file(一个路径path),raw(text)
     *
     * @param resBody 请求体，form(根据是否选择file自动改变请求头类型)(key/type(text/file)/value/desc/enable),json,file(一个路径path),raw(text)
     */
    @Column(name = "res_body")
    var resBody: String? = null

    /**
     * 创建时间
     */
    /**
     * 获取创建时间
     *
     * @return cdate - 创建时间
     */
    /**
     * 设置创建时间
     *
     * @param cdate 创建时间
     */
    var cdate: Int? = null

    /**
     * 更新时间
     */
    /**
     * 获取更新时间
     *
     * @return udate - 更新时间
     */
    /**
     * 设置更新时间
     *
     * @param udate 更新时间
     */
    var udate: Int? = null

    /**
     * 创建人ID
     */
    /**
     * 获取创建人ID
     *
     * @return cuid - 创建人ID
     */
    /**
     * 设置创建人ID
     *
     * @param cuid 创建人ID
     */
    var cuid: Long? = null
}
