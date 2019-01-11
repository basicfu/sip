package com.basicfu.sip.api.model.po

import javax.persistence.*

@Table(name = "project_category")
class ProjectCategory {
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
     * 项目Id
     */
    /**
     * 获取项目Id
     *
     * @return project_id - 项目Id
     */
    /**
     * 设置项目Id
     *
     * @param projectId 项目Id
     */
    @Column(name = "project_id")
    var projectId: Long? = null

    /**
     * 父级Id
     */
    /**
     * 获取父级Id
     *
     * @return pid - 父级Id
     */
    /**
     * 设置父级Id
     *
     * @param pid 父级Id
     */
    var pid: Long? = null

    /**
     * 分类名
     */
    /**
     * 获取分类名
     *
     * @return name - 分类名
     */
    /**
     * 设置分类名
     *
     * @param name 分类名
     */
    var name: String? = null

    /**
     * 分类顺序
     */
    /**
     * 获取分类顺序
     *
     * @return sort - 分类顺序
     */
    /**
     * 设置分类顺序
     *
     * @param sort 分类顺序
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
