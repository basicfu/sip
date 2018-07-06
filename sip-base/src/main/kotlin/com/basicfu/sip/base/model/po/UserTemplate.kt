package com.basicfu.sip.base.model.po

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "user_template")
class UserTemplate {
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
     * 租户ID
     */
    /**
     * 获取租户ID
     *
     * @return tenant_id - 租户ID
     */
    /**
     * 设置租户ID
     *
     * @param tenantId 租户ID
     */
    @Column(name = "tenant_id")
    var tenantId: Long? = null

    /**
     * 字段名
     */
    /**
     * 获取字段名
     *
     * @return name - 字段名
     */
    /**
     * 设置字段名
     *
     * @param name 字段名
     */
    var name: String? = null
    var enName: String? = null

    /**
     * 字段类型(0Text,1Number,2Check,3Radio,4Date,5Time,6DateTime,7Dict)
     */
    /**
     * 获取字段类型(0Text,1Number,2Check,3Radio,4Date,5Time,6DateTime,7Dict)
     *
     * @return type - 字段类型(0Text,1Number,2Check,3Radio,4Date,5Time,6DateTime,7Dict)
     */
    /**
     * 设置字段类型(0Text,1Number,2Check,3Radio,4Date,5Time,6DateTime,7Dict)
     *
     * @param type 字段类型(0Text,1Number,2Check,3Radio,4Date,5Time,6DateTime,7Dict)
     */
    var type: String? = null

    /**
     * 字段长度(Text限制字段长度,Number限制字段范围大小(-100~300),字典存储关联的字典,其他不限)
     */
    /**
     * 获取字段长度(Text限制字段长度,Number限制字段范围大小(-100~300),字典存储关联的字典,其他不限)
     *
     * @return extra - 字段长度(Text限制字段长度,Number限制字段范围大小(-100~300),字典存储关联的字典,其他不限)
     */
    /**
     * 设置字段长度(Text限制字段长度,Number限制字段范围大小(-100~300),字典存储关联的字典,其他不限)
     *
     * @param extra 字段长度(Text限制字段长度,Number限制字段范围大小(-100~300),字典存储关联的字典,其他不限)
     */
    var extra: String? = null
    var defaultValue: String? = null
    var required:Boolean?=null
    var sort: Int? = null

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
