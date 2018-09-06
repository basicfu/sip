package com.basicfu.sip.core.model.po

import javax.persistence.*

class Dict {
    /**
     * 主键
     */
    /**
     * 获取主键
     *
     * @return id - 主键
     */
    /**
     * 设置主键
     *
     * @param id 主键
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
     * 字典名
     */
    /**
     * 获取字典名
     *
     * @return name - 字典名
     */
    /**
     * 设置字典名
     *
     * @param name 字典名
     */
    var name: String? = null

    /**
     * 字典值
     */
    /**
     * 获取字典值
     *
     * @return value - 字典值
     */
    /**
     * 设置字典值
     *
     * @param value 字典值
     */
    var value: String? = null

    /**
     * 字典描述
     */
    /**
     * 获取字典描述
     *
     * @return description - 字典描述
     */
    /**
     * 设置字典描述
     *
     * @param description 字典描述
     */
    var description: String? = null

    /**
     * 左节点
     */
    /**
     * 获取左节点
     *
     * @return lft - 左节点
     */
    /**
     * 设置左节点
     *
     * @param lft 左节点
     */
    var lft: Long? = null

    /**
     * 右节点
     */
    /**
     * 获取右节点
     *
     * @return rgt - 右节点
     */
    /**
     * 设置右节点
     *
     * @param rgt 右节点
     */
    var rgt: Long? = null

    /**
     * 节点层级
     */
    /**
     * 获取节点层级
     *
     * @return lvl - 节点层级
     */
    /**
     * 设置节点层级
     *
     * @param lvl 节点层级
     */
    var lvl: Int? = null

    /**
     * 排序
     */
    /**
     * 获取排序
     *
     * @return sort - 排序
     */
    /**
     * 设置排序
     *
     * @param sort 排序
     */
    var sort: Int? = null

    /**
     * 能否固定 0否,1是
     */
    /**
     * 获取能否固定 0否,1是
     *
     * @return fixed - 能否固定 0否,1是
     */
    /**
     * 设置能否固定 0否,1是
     *
     * @param fixed 能否固定 0否,1是
     */
    var fixed: Boolean? = null

    /**
     * 逻辑删除 0否,1是
     */
    /**
     * 获取逻辑删除 0否,1是
     *
     * @return isdel - 逻辑删除 0否,1是
     */
    /**
     * 设置逻辑删除 0否,1是
     *
     * @param isdel 逻辑删除 0否,1是
     */
    var isdel: Boolean? = null
}
