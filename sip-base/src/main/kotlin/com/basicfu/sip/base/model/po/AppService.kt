package com.basicfu.sip.base.model.po

import javax.persistence.*

@Table(name = "app_service")
class AppService {
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
     * 应用ID
     */
    /**
     * 获取应用ID
     *
     * @return app_id - 应用ID
     */
    /**
     * 设置应用ID
     *
     * @param appId 应用ID
     */
    @Column(name = "app_id")
    var appId: Long? = null

    /**
     * 应用名
     */
    /**
     * 获取应用名
     *
     * @return name - 应用名
     */
    /**
     * 设置应用名
     *
     * @param name 应用名
     */
    var name: String? = null

    /**
     * 应用path
     */
    /**
     * 获取应用path
     *
     * @return path - 应用path
     */
    /**
     * 设置应用path
     *
     * @param path 应用path
     */
    var path: String? = null

    /**
     * 应用注册名
     */
    /**
     * 获取应用注册名
     *
     * @return server_id - 应用注册名
     */
    /**
     * 设置应用注册名
     *
     * @param serverId 应用注册名
     */
    @Column(name = "server_id")
    var serverId: String? = null

    /**
     * 应用URL
     */
    /**
     * 获取应用URL
     *
     * @return url - 应用URL
     */
    /**
     * 设置应用URL
     *
     * @param url 应用URL
     */
    var url: String? = null

    /**
     * 过滤前缀
     */
    /**
     * 获取过滤前缀
     *
     * @return strip_prefix - 过滤前缀
     */
    /**
     * 设置过滤前缀
     *
     * @param stripPrefix 过滤前缀
     */
    @Column(name = "strip_prefix")
    var stripPrefix: Boolean? = null

    /**
     * 重试
     */
    /**
     * 获取重试
     *
     * @return retryable - 重试
     */
    /**
     * 设置重试
     *
     * @param retryable 重试
     */
    var retryable: Boolean? = null

    /**
     * 敏感头信息
     */
    /**
     * 获取敏感头信息
     *
     * @return sensitive_headers - 敏感头信息
     */
    /**
     * 设置敏感头信息
     *
     * @param sensitiveHeaders 敏感头信息
     */
    @Column(name = "sensitive_headers")
    var sensitiveHeaders: String? = null

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
}
