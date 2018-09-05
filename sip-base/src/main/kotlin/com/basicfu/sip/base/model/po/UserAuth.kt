package com.basicfu.sip.base.model.po

import javax.persistence.*

@Table(name = "user_auth")
class UserAuth {
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
     * @return app_id
     */
    /**
     * @param appId
     */
    @Column(name = "app_id")
    var appId: Long? = null

    /**
     * auth类型0用户名,1手机号,2字典(读取字典)
     */
    /**
     * 获取auth类型0用户名,1手机号,2字典(读取字典)
     *
     * @return type - auth类型0用户名,1手机号,2字典(读取字典)
     */
    /**
     * 设置auth类型0用户名,1手机号,2字典(读取字典)
     *
     * @param type auth类型0用户名,1手机号,2字典(读取字典)
     */
    var type: Byte? = null

    /**
     * 登录标识
     */
    /**
     * 获取登录标识
     *
     * @return username - 登录标识
     */
    /**
     * 设置登录标识
     *
     * @param username 登录标识
     */
    var username: String? = null

    /**
     * 密码凭证
     */
    /**
     * 获取密码凭证
     *
     * @return password - 密码凭证
     */
    /**
     * 设置密码凭证
     *
     * @param password 密码凭证
     */
    var password: String? = null

    /**
     * 盐值
     */
    /**
     * 获取盐值
     *
     * @return salt - 盐值
     */
    /**
     * 设置盐值
     *
     * @param salt 盐值
     */
    var salt: String? = null

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
     * 最后一次登录时间
     */
    /**
     * 获取最后一次登录时间
     *
     * @return ldate - 最后一次登录时间
     */
    /**
     * 设置最后一次登录时间
     *
     * @param ldate 最后一次登录时间
     */
    var ldate: Int? = null

    /**
     * 用户ID
     */
    /**
     * 获取用户ID
     *
     * @return uid - 用户ID
     */
    /**
     * 设置用户ID
     *
     * @param uid 用户ID
     */
    var uid: Long? = null
}
