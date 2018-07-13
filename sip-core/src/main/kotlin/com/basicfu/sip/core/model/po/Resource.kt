package com.basicfu.sip.core.model.po

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class Resource {
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
     * 服务Id
     */
    /**
     * 获取服务Id
     *
     * @return service_id - 服务Id
     */
    /**
     * 设置服务Id
     *
     * @param serviceId 服务Id
     */
    @Column(name = "service_id")
    var serviceId: Long? = null

    /**
     * 资源URL
     */
    /**
     * 获取资源URL
     *
     * @return url - 资源URL
     */
    /**
     * 设置资源URL
     *
     * @param url 资源URL
     */
    var url: String? = null

    /**
     * 资源名
     */
    /**
     * 获取资源名
     *
     * @return name - 资源名
     */
    /**
     * 设置资源名
     *
     * @param name 资源名
     */
    var name: String? = null
    var method:String?=null
}
