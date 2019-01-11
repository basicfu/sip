package com.basicfu.sip.api.model.po

import javax.persistence.*

class Project {
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
     * 项目名
     */
    /**
     * 获取项目名
     *
     * @return name - 项目名
     */
    /**
     * 设置项目名
     *
     * @param name 项目名
     */
    var name: String? = null

    /**
     * 基础路径
     */
    /**
     * 获取基础路径
     *
     * @return base_path - 基础路径
     */
    /**
     * 设置基础路径
     *
     * @param basePath 基础路径
     */
    @Column(name = "base_path")
    var basePath: String? = null

    /**
     * 项目类型，公开、私有
     */
    /**
     * 获取项目类型，公开、私有
     *
     * @return project_type - 项目类型，公开、私有
     */
    /**
     * 设置项目类型，公开、私有
     *
     * @param projectType 项目类型，公开、私有
     */
    @Column(name = "project_type")
    var projectType: String? = null

    /**
     * 成员数组，uid
     */
    /**
     * 获取成员数组，uid
     *
     * @return members - 成员数组，uid
     */
    /**
     * 设置成员数组，uid
     *
     * @param members 成员数组，uid
     */
    var members: String? = null

    /**
     * 环境数组，name、url、header、cookie、global
     */
    /**
     * 获取环境数组，name、url、header、cookie、global
     *
     * @return env - 环境数组，name、url、header、cookie、global
     */
    /**
     * 设置环境数组，name、url、header、cookie、global
     *
     * @param env 环境数组，name、url、header、cookie、global
     */
    var env: String? = null

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
