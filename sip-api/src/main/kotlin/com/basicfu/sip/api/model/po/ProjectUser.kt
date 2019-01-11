package com.basicfu.sip.api.model.po

import javax.persistence.*

@Table(name = "project_user")
class ProjectUser {
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
     * 用户id
     */
    /**
     * 获取用户id
     *
     * @return uid - 用户id
     */
    /**
     * 设置用户id
     *
     * @param uid 用户id
     */
    var uid: Long? = null

    /**
     * 项目id
     */
    /**
     * 获取项目id
     *
     * @return project_id - 项目id
     */
    /**
     * 设置项目id
     *
     * @param projectId 项目id
     */
    @Column(name = "project_id")
    var projectId: Long? = null

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
