package com.basicfu.sip.getway.mapper

import com.basicfu.sip.getway.model.Resource
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

/**
 * @author basicfu
 * @date 2018/7/12
 */
@Mapper
interface Mapper {

    @Select("SELECT id,service_id as serviceId,url FROM resource")
    fun selectResource(): List<Resource>

    @Select("SELECT pr.resource_id FROM role r LEFT JOIN role_permission rp on r.id=rp.role_id INNER JOIN permission_resource pr ON rp.permission_id=pr.permission_id where r.code =#{code}")
    fun selectMenuResourceIdByRoleCode(@Param("code") code: String): List<Long>

    @Select("SELECT mr.resource_id FROM role r LEFT JOIN role_menu rm on r.id=rm.role_id INNER JOIN menu_resource mr ON rm.menu_id=mr.menu_id where r.code =#{code}")
    fun selectPermissionResourceIdByRoleCode(@Param("code") code: String): List<Long>
}
