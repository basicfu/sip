package com.basicfu.sip.base.model.po

import javax.persistence.*

@Table(name = "app_secret")
class AppSecret {
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
     * secret
     */
    /**
     * 获取secret
     *
     * @return secret - secret
     */
    /**
     * 设置secret
     *
     * @param secret secret
     */
    var secret: String? = null

    /**
     * 描述
     */
    /**
     * 获取描述
     *
     * @return description - 描述
     */
    /**
     * 设置描述
     *
     * @param description 描述
     */
    var description: String? = null

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
