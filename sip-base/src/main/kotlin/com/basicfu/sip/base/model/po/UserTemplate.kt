package com.basicfu.sip.base.model.po

import javax.persistence.*

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
     * @return app_id - 租户ID
     */
    /**
     * 设置租户ID
     *
     * @param appId 租户ID
     */
    @Column(name = "app_id")
    var appId: Long? = null

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

    /**
     * 字段英文名
     */
    /**
     * 获取字段英文名
     *
     * @return en_name - 字段英文名
     */
    /**
     * 设置字段英文名
     *
     * @param enName 字段英文名
     */
    @Column(name = "en_name")
    var enName: String? = null

    /**
     * 字段类型(0TEXT,1NUMBER,2CHECK,3RADIO,4SELECT,5DATE)
     */
    /**
     * 获取字段类型(0TEXT,1NUMBER,2CHECK,3RADIO,4SELECT,5DATE)
     *
     * @return type - 字段类型(0TEXT,1NUMBER,2CHECK,3RADIO,4SELECT,5DATE)
     */
    /**
     * 设置字段类型(0TEXT,1NUMBER,2CHECK,3RADIO,4SELECT,5DATE)
     *
     * @param type 字段类型(0TEXT,1NUMBER,2CHECK,3RADIO,4SELECT,5DATE)
     */
    var type: String? = null

    /**
     * 字段长度(TEXT限制字段长度范围,NUMBER限制字段长度且范围大小10,2(0.15~255.25),CHECK/RADIO/SELECT存储关联的字典,DATA自定义格式化时间yyyy-MM-dd HH:mm:ss)
     */
    /**
     * 获取字段长度(TEXT限制字段长度范围,NUMBER限制字段长度且范围大小10,2(0.15~255.25),CHECK/RADIO/SELECT存储关联的字典,DATA自定义格式化时间yyyy-MM-dd HH:mm:ss)
     *
     * @return extra - 字段长度(TEXT限制字段长度范围,NUMBER限制字段长度且范围大小10,2(0.15~255.25),CHECK/RADIO/SELECT存储关联的字典,DATA自定义格式化时间yyyy-MM-dd HH:mm:ss)
     */
    /**
     * 设置字段长度(TEXT限制字段长度范围,NUMBER限制字段长度且范围大小10,2(0.15~255.25),CHECK/RADIO/SELECT存储关联的字典,DATA自定义格式化时间yyyy-MM-dd HH:mm:ss)
     *
     * @param extra 字段长度(TEXT限制字段长度范围,NUMBER限制字段长度且范围大小10,2(0.15~255.25),CHECK/RADIO/SELECT存储关联的字典,DATA自定义格式化时间yyyy-MM-dd HH:mm:ss)
     */
    var extra: String? = null

    /**
     * 字段默认值
     */
    /**
     * 获取字段默认值
     *
     * @return default_value - 字段默认值
     */
    /**
     * 设置字段默认值
     *
     * @param defaultValue 字段默认值
     */
    @Column(name = "default_value")
    var defaultValue: String? = null

    /**
     * 是否必填
     */
    /**
     * 获取是否必填
     *
     * @return required - 是否必填
     */
    /**
     * 设置是否必填
     *
     * @param required 是否必填
     */
    var required: Boolean? = null

    /**
     * 字段顺序
     */
    /**
     * 获取字段顺序
     *
     * @return sort - 字段顺序
     */
    /**
     * 设置字段顺序
     *
     * @param sort 字段顺序
     */
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
