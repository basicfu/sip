package com.basicfu.sip.logs.modal.po

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "warn_rule")
class WarnRule {
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
     * 规则名
     */
    /**
     * 获取规则名
     *
     * @return name - 规则名
     */
    /**
     * 设置规则名
     *
     * @param name 规则名
     */
    var name: String? = null

    /**
     * 模板下的字段英文名
     */
    /**
     * 获取模板下的字段英文名
     *
     * @return en_name - 模板下的字段英文名
     */
    /**
     * 设置模板下的字段英文名
     *
     * @param enName 模板下的字段英文名
     */
    @Column(name = "en_name")
    var enName: String? = null

    /**
     * 条件字典(>=,>,<=,<,=,!=,equals,not equals)
     */
    /**
     * 获取条件字典(>=,>,<=,<,=,!=,equals,not equals)
     *
     * @return condition - 条件字典(>=,>,<=,<,=,!=,equals,not equals)
     */
    /**
     * 设置条件字典(>=,>,<=,<,=,!=,equals,not equals)
     *
     * @param condition 条件字典(>=,>,<=,<,=,!=,equals,not equals)
     */
    var condition: String? = null

    /**
     * 条件值
     */
    /**
     * 获取条件值
     *
     * @return value - 条件值
     */
    /**
     * 设置条件值
     *
     * @param value 条件值
     */
    var value: String? = null

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
