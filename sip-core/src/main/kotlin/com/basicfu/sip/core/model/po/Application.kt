package com.basicfu.sip.core.model.po

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class Application {
    /**
     * 主键
     */
    /**
     * 获取主键
     *
     * @return id - 主键
     */
    /**
     * 设置主键
     *
     * @param id 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

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
}