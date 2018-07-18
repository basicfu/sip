package com.basicfu.sip.getway.mapper

import com.basicfu.sip.core.model.dto.MenuDto
import com.basicfu.sip.core.model.po.App
import com.basicfu.sip.core.model.po.Resource
import com.basicfu.sip.core.model.po.Service
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

/**
 * @author basicfu
 * @date 2018/7/12
 */
@Mapper
interface Mapper {

    @Select("SELECT id,name,domain from app")
    fun selectApp(): List<App>

    @Select("SELECT id,app_id as appId,name,path,server_id as serverId,url,strip_prefix as stripPrefix,retryable,sensitive_headers as sensitiveHeaders from service")
    fun selectService(): List<Service>

    @Select("SELECT m.id,m.app_id as appId,m.pid,m.name,m.path,m.sort,m.icon,m.type,m.display FROM role r LEFT JOIN role_menu rm on r.id=rm.role_id LEFT JOIN menu m on rm.menu_id=m.id WHERE r.code='#{code}'")
    fun selectMenuByRoleCode(@Param("code") code:String): List<MenuDto>

    @Select("SELECT resource.service_id as serviceId,resource.url as url,resource.method as method FROM role r LEFT JOIN role_permission rp on r.id=rp.role_id INNER JOIN permission_resource pr ON rp.permission_id=pr.permission_id LEFT JOIN resource on resource.id=pr.resource_id where r.code=#{code}")
    fun selectMenuResourceIdByRoleCode(@Param("code") code: String): List<Resource>

    @Select("SELECT resource.service_id as serviceId,resource.url as url,resource.method as method FROM role r LEFT JOIN role_menu rm on r.id=rm.role_id INNER JOIN menu_resource mr ON rm.menu_id=mr.menu_id LEFT JOIN resource on resource.id=mr.resource_id where r.code=#{code}")
    fun selectPermissionResourceIdByRoleCode(@Param("code") code: String): List<Resource>
}
