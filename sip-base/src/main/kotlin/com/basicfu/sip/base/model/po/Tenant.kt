package com.basicfu.sip.base.model.po

import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class Tenant {
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
     * 租户企业名
     */
    /**
     * 获取租户企业名
     *
     * @return name - 租户企业名
     */
    /**
     * 设置租户企业名
     *
     * @param name 租户企业名
     */
    var name: String? = null

    /**
     * 租户域名前缀
     */
    /**
     * 获取租户域名前缀
     *
     * @return domain - 租户域名前缀
     */
    /**
     * 设置租户域名前缀
     *
     * @param domain 租户域名前缀
     */
    var domain: String? = null
}