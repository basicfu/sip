package com.basicfu.sip.dict.service

import com.basicfu.sip.common.enum.Enum
import com.basicfu.sip.common.model.dto.DictDto
import com.basicfu.sip.common.model.po.Dict
import com.basicfu.sip.common.model.vo.DictVo
import com.basicfu.sip.core.common.exception.CustomException
import com.basicfu.sip.core.common.mapper.example
import com.basicfu.sip.core.common.mapper.generate
import com.basicfu.sip.core.service.BaseService
import com.basicfu.sip.dict.mapper.DictMapper
import com.github.pagehelper.PageInfo
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service
import tk.mybatis.mapper.entity.Example
import java.lang.StringBuilder
import java.util.*

/**
 * @author basicfu
 * @date 2018/6/25
 */
@Service
class DictService : BaseService<DictMapper, Dict>() {
    private final val defaultSplitDelimiter = "-"

    fun all(): DictDto {
        val all = to<DictDto>(mapper.selectByExample(example<Dict> {
            andEqualTo(Dict::isdel, false)
        }))
        val root = all.filter { it.lvl == 0 }[0]
        chidren(root, all)
        return root
    }

    /**
     * 列出第一层级字典
     */
    fun list(vo: DictVo): PageInfo<DictDto> {
        return selectPage(
            example<Dict> {
                andEqualTo {
                    name = vo.name
                    lvl = 1
                    isdel = false
                }
            }
        )
    }

    fun get(value: String): List<DictDto> {
        val list =
            to<DictDto>(mapper.selectBySql("SELECT d1.id,d1.name,d1.value,d1.lft,d1.rgt,d1.lvl,d1.fixed,d1.sort FROM dict AS d1,dict AS d2 WHERE d1.lft > d2.lft AND d1.lft<d2.rgt AND d2.value = '$value' and d1.isdel=0"))
        val result = list.filter { it.lvl == 2 }.toMutableList()
        result.forEach {
            chidren(it, list)
        }
        result.sortBy { it.sort }
        return result
    }

    /**
     * 叶子节点下增加节点，添加到子节点的最后一个
     */
    fun insert(vo: DictVo): Int {
        val parentDict = mapper.selectOneByExample(example<Dict> {
            select(Dict::value, Dict::rgt, Dict::lvl)
            forUpdate()
            andEqualTo {
                id = vo.pid
                isdel = false
            }
        }) ?: throw CustomException(Enum.NOT_FOUND_DICT)
        val count = mapper.selectCountBySql(
            "SELECT count(*) FROM dict AS d1,dict AS d2 " +
                    "WHERE d1.lft > d2.lft AND d1.lft<d2.rgt AND d2.value = '" + parentDict.value + "' AND d1.lvl=d2.lvl+1 and d1.value='" + vo.value + "' and d1.isdel=0"
        )
        if (count != 0) {
            throw CustomException(Enum.EXIST_DICT_VALUE)
        }
        mapper.updateBySql("set rgt=rgt+2 where rgt>=${parentDict.rgt} and isdel=0")
        mapper.updateBySql("set lft=lft+2 where lft>${parentDict.rgt} and isdel=0")
        val po = Dict()
        po.name = vo.name
        po.value = vo.value
        po.description = vo.description
        po.fixed = vo.fixed
        po.lft = parentDict.rgt!!
        po.rgt = parentDict.rgt!! + 1
        po.lvl = parentDict.lvl!! + 1
        po.sort = vo.sort
        mapper.insertSelective(po)
        return 1
    }

    /**
     * 只能更新name和是否编辑
     * 后期可以添加支持修改节点
     */
    fun update(vo: DictVo): Int {
        val po = generate<Dict> {
            id = vo.id
            name = vo.name
            sort = vo.sort
            fixed = vo.fixed
        }
        return mapper.updateByPrimaryKeySelective(po)
    }

    /**
     * 删除节点自动删除子节点
     */
    fun delete(ids: List<Long>): Int {
        ids.forEach { id ->
            if (id == 1L) {
                throw CustomException(Enum.NO_DELETE_ROOT_DICT)
            }
            val example = Example(Dict::class.java)
            example.selectProperties("lft", "rgt")
            example.isForUpdate = true
            example.createCriteria().andEqualTo("id", id).andEqualTo("isdel", 0)
            val one = mapper.selectByExample(example)
            if (!one.isEmpty()) {
                val tmp = one[0]
                val width = tmp.rgt!! - tmp.lft!! + 1
                val updatePo = Dict()
                updatePo.isdel = true
                val exampleDelete = Example(Dict::class.java)
                exampleDelete.and().andBetween("lft", tmp.lft, tmp.rgt)
                mapper.updateByExampleSelective(updatePo, exampleDelete)
                mapper.updateBySql("set rgt = rgt -$width where rgt>${tmp.rgt} and isdel=0")
                mapper.updateBySql("set lft = lft -$width where lft>${tmp.rgt} and isdel=0")
            }
        }
        return 1
    }

    /**
     * 导入
     * 格式：
     * 层级(n-1)个-value-描述-是否固定-顺序
     */
    fun import(vo: DictVo) {
        if (vo.value.isNullOrBlank()) {
            throw CustomException(Enum.IMPORT_FORMAT_ERROR)
        }
        val splitDelimiter = if (vo.splitDelimiter == null) {
            defaultSplitDelimiter
        } else {
            vo.splitDelimiter!!
        }
        val list = vo.value?.split("\n")?.filter { it.isNotBlank() }
        recursiveImport(splitDelimiter, null, list)
    }

    fun export(vo: DictVo): String {
        val splitDelimiter = if (vo.splitDelimiter == null) {
            defaultSplitDelimiter
        } else {
            vo.splitDelimiter!!
        }
        val dict = all()
        return StringUtils.join(chidrenToString(splitDelimiter, dict.children), "\n")
    }

    private fun recursiveImport(splitDelimiter: String, preParentDict: Dict?, list: List<String>?) {
        if (list == null) {
            return
        }
        for ((index, it) in list.withIndex()) {
            //第一条必须不能为子项
            if (preParentDict == null && index == 0 && it.startsWith(splitDelimiter)) throw CustomException(
                Enum.IMPORT_FORMAT_ERROR
            )
            val arrays = arrayListOf<String>()
            val level = if (preParentDict != null) {
                arrays.addAll(it.drop(preParentDict.lvl!!).split(splitDelimiter))
                preParentDict.lvl!!
            } else {
                arrays.addAll(it.split(splitDelimiter))
                0
            }
            //每一层只处理同级层
            if ((level == 0 && it.startsWith(splitDelimiter)) || level != 0 && !(it.startsWith(
                    spliceStr(
                        splitDelimiter,
                        level
                    )
                ) && !it.startsWith(spliceStr(splitDelimiter, level), 1))
            ) {
                continue
            }
            if (arrays.size <= 2) throw CustomException(Enum.NOT_NULL_DICT_NAME_AND_VALUE)
            val itemName = arrays[0]
            val itemValue = arrays[1]
            val itemDescrption = if (arrays.size >= 3) arrays[2] else null
            val itemFixed = if (arrays.size >= 4) arrays[3] else null
            val itemSort = if (arrays.size >= 5) arrays[4] else null
            //查询当前菜单是否存在
            var parentDict = mapper.selectOneByExample(example<Dict> {
                forUpdate()
                andEqualTo {
                    value = itemValue
                    lvl = level + 1
                    isdel = false
                }
                if (preParentDict != null) {
                    andGreaterThan(Dict::lft, preParentDict.lft!!.toString())
                    andLessThan(Dict::lft, preParentDict.rgt!!.toString())
                }
            }) ?: null
            //不存在查询父菜单信息并新建
            if (parentDict == null) {
                val newParentDict = mapper.selectOneByExample(example<Dict> {
                    if (preParentDict != null) {
                        andEqualTo {
                            id = preParentDict.id
                        }
                    } else {
                        andEqualTo {
                            lvl = level
                            isdel = false
                        }
                    }
                })
                mapper.updateBySql("set rgt=rgt+2 where rgt>=${newParentDict.rgt} and isdel=0")
                mapper.updateBySql("set lft=lft+2 where lft>${newParentDict.rgt} and isdel=0")
                val po = generate<Dict> {
                    name = itemName
                    value = itemValue
                    description = itemDescrption
                    fixed = itemFixed?.toBoolean()
                    sort = itemSort?.toInt()
                    lft = newParentDict.rgt
                    rgt = newParentDict.rgt!! + 1
                    lvl = level + 1
                }
                mapper.insertSelective(po)
                parentDict = po
            }
            recursiveImport(splitDelimiter, parentDict, findChild(splitDelimiter, list.drop(index + 1), level + 1))
        }
    }

    private fun findChild(splitDelimiter: String, list: List<String>, level: Int): List<String> {
        val childList = arrayListOf<String>()
        for (s in list) {
            if (!s.startsWith(spliceStr(splitDelimiter, level))) {
                break
            }
            childList.add(s)
        }
        return childList
    }

    private fun spliceStr(str: String, num: Int): String {
        if (num == 0) {
            return ""
        }
        val result = StringBuilder()
        for (i in 1..num) {
            result.append(str)
        }
        return result.toString()
    }

    private fun chidren(parent: DictDto, list: List<DictDto>) {
        val children = ArrayList<DictDto>()
        list.filter { it.lft in parent.lft!!..parent.rgt!! && it.lvl == parent.lvl!! + 1 }.forEach {
            chidren(it, list)
            children.add(it)
        }
        children.sortBy { it.sort }
        parent.children = children
    }

    private fun chidrenToString(splitDelimiter: String, list: List<DictDto>?): List<String> {
        val result = arrayListOf<String>()
        list?.forEach {
            result.add(
                spliceStr(
                    splitDelimiter,
                    it.lvl!! - 1
                ) + it.name + splitDelimiter + it.value + splitDelimiter + it.description + splitDelimiter + it.fixed + splitDelimiter + it.sort
            )
            result.addAll(chidrenToString(splitDelimiter, it.children))
        }
        return result
    }
}
