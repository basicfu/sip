package com.basicfu.sip.dict.service

import com.basicfu.sip.core.exception.CustomException
import com.basicfu.sip.core.mapper.example
import com.basicfu.sip.core.model.dto.DictDto
import com.basicfu.sip.core.model.po.Dict
import com.basicfu.sip.core.model.vo.DictVo
import com.basicfu.sip.core.service.BaseService
import com.basicfu.sip.dict.common.Enum
import com.basicfu.sip.dict.mapper.DictMapper
import com.github.pagehelper.PageInfo
import org.springframework.beans.BeanUtils
import org.springframework.stereotype.Service
import tk.mybatis.mapper.entity.Example

/**
 * @author basicfu
 * @date 2018/6/25
 */
@Service
class DictService : BaseService<DictMapper, Dict>() {

    fun all(): List<Any> {
        val all = to<DictDto>(mapper.selectByExample(example<Dict> {
            andEqualTo(Dict::isdel, "0")
        }))
        val result = ArrayList<DictDto>()
        all.filter { it.lvl == 0 }.forEach { e ->
            val dto = DictDto()
            BeanUtils.copyProperties(e, dto)
            dto.value = dto.id.toString()
            dto.pid = 0
            result.add(dto)
        }
        result.forEach { e ->
            val children = ArrayList<DictDto>()
            all.forEach { ee ->
                if (ee.lft in e.lft!!..e.rgt!! && ee.lvl == e.lvl!! + 1) {
                    val dto = DictDto()
                    BeanUtils.copyProperties(ee, dto)
                    chidren(dto, all)
                    dto.value = dto.id.toString()
                    dto.pid = e.id
                    children.add(dto)
                }
            }
            e.children = children
        }
        return result
    }

    fun list(vo: DictVo): PageInfo<DictDto> {
        return selectPage(
            example<Dict> {
                andEqualTo {
                    name = vo.name
                    lvl=1
                    isdel = 0
                }
            }
        )
    }
    fun get(value: String): List<DictDto> {
        val list=to<DictDto>(mapper.selectBySql("SELECT d1.id,d1.name,d1.value,d1.lft,d1.rgt,d1.lvl,d1.fixed FROM dict AS d1,dict AS d2 WHERE d1.lft > d2.lft AND d1.lft<d2.rgt AND d2.value = '$value' and d1.isdel=0"))
        val result = ArrayList<DictDto>()
        list.filter { it.lvl==2 }.forEach { item->
            val children = ArrayList<DictDto>()
            list.filter { it.lft in item.lft!!..item.rgt!! && it.lvl == item.lvl!! + 1 }.forEach {dict->
                chidren(dict, list)
                dict.value = dict.id.toString()
                dict.pid = item.id
                children.add(dict)
            }
            item.children = children
            result.add(item)
        }
        return result
    }
    /**
     * 叶子节点下增加节点，添加到子节点的最后一个
     */
    fun insert(vo: DictVo): Int {
        val parentDict= mapper.selectOneByExample(example<Dict>{
            select(Dict::value,Dict::rgt,Dict::lvl)
            forUpdate()
            andEqualTo {
                id=vo.pid
                isdel=0
            }
        }) ?: throw CustomException(Enum.Dict.NOT_FOUND)
        val count = mapper.selectCountBySql(
            "SELECT count(*) FROM dict AS d1,dict AS d2 " +
                    "WHERE d1.lft > d2.lft AND d1.lft<d2.rgt AND d2.value = '"+parentDict.value+"' AND d1.lvl=d2.lvl+1 and d1.value='"+vo.value+"' and d1.isdel=0"
        )
        if(count!=0){
            throw CustomException(Enum.Dict.VALUE_REPEAT)
        }
        mapper.updateBySql("set rgt=rgt+2 where rgt>=${parentDict.rgt} and isdel=0")
        mapper.updateBySql("set lft=lft+2 where lft>${parentDict.rgt} and isdel=0")
        val po = Dict()
        po.name = vo.name
        po.value=vo.value
        po.description=vo.description
        po.fixed = vo.fixed
        po.lft = parentDict.rgt!!
        po.rgt = parentDict.rgt!! + 1
        po.lvl = parentDict.lvl!! + 1
        mapper.insertSelective(po)
        return 1
    }

    /**
     * 只能更新name和是否编辑
     * 后期可以添加支持修改节点
     */
    fun update(vo: DictVo): Int {
        val po = Dict()
        po.id = vo.id
        po.name = vo.name
        po.fixed = vo.fixed
        return mapper.updateByPrimaryKeySelective(po)
    }

    /**
     * 删除节点自动删除子节点
     */
    fun delete(ids: List<Long>): Int {
        ids.forEach { id ->
            if (id == 1L) {
                throw CustomException(Enum.Dict.NO_DELETE_ROOT)
            }
            val example = Example(Dict::class.java)
            example.selectProperties("lft,rgt").isForUpdate = true
            example.createCriteria().andEqualTo("id", id).andEqualTo("isdel", 0)
            val one = mapper.selectByExample(example)
            if (!one.isEmpty()) {
                val tmp = one[0]
                val width = tmp.rgt!! - tmp.lft!! + 1
                val updatePo = Dict()
                updatePo.isdel = 1
                val exampleDelete = Example(Dict::class.java)
                exampleDelete.and().andBetween("lft", tmp.lft, tmp.rgt)
                mapper.updateByExampleSelective(updatePo, exampleDelete)
                mapper.updateBySql("set rgt = rgt -$width where rgt>${tmp.rgt} and isdel=0")
                mapper.updateBySql("set lft = lft -$width where lft>${tmp.rgt} and isdel=0")
            }
        }
        return 1
    }

    fun chidren(item: DictDto, list: List<DictDto>) {
        val children = ArrayList<DictDto>()
        list.filter { it.lft in item.lft!!..item.rgt!! && it.lvl == item.lvl!! + 1 }.forEach {dict->
            chidren(dict, list)
            dict.value = dict.id.toString()
            dict.pid = item.id
            children.add(dict)
        }
        item.children = children
    }
}