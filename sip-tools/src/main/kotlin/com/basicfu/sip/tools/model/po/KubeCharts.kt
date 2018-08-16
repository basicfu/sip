package com.basicfu.sip.tools.model.po

import javax.persistence.*

@Table(name = "kube_charts")
class KubeCharts {
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
     * chart名
     */
    /**
     * 获取chart名
     *
     * @return name - chart名
     */
    /**
     * 设置chart名
     *
     * @param name chart名
     */
    var name: String? = null

    /**
     * 命名空间
     */
    /**
     * 获取命名空间
     *
     * @return namespace - 命名空间
     */
    /**
     * 设置命名空间
     *
     * @param namespace 命名空间
     */
    var namespace: String? = null

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
     * chart values
     */
    /**
     * 获取chart values
     *
     * @return values - chart values
     */
    /**
     * 设置chart values
     *
     * @param values chart values
     */
    @Column(name="`values`")
    var values: String? = null
}
