package com.basicfu.sip.getway.mapper

import com.basicfu.sip.common.model.dto.*
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

/**
 * @author basicfu
 * @date 2018/7/12
 */
@Mapper
interface Mapper {

    @Select("SELECT id,name,code from app")
    fun selectApp(): List<AppDto>

    @Select("SELECT id,app_id as appId,name,path,server_id as serverId,url,strip_prefix as stripPrefix,retryable,sensitive_headers as sensitiveHeaders from app_service")
    fun selectService(): List<AppServiceDto>

    @Select("SELECT id,app_id as appId,secret,description,cdate,udate from app_secret")
    fun selectSecret(): List<AppSecretDto>

    @Select("SELECT m.id as id,m.app_id as appId,m.pid as pid,m.name as name,m.path as path,m.sort as sort,m.icon as icon,m.type as type,m.display as display FROM role r INNER JOIN role_menu rm on r.id=rm.role_id INNER JOIN menu m on rm.menu_id=m.id WHERE r.code=#{code}")
    fun selectMenuByRoleCode(@Param("code") code: String): List<MenuDto>

    @Select("SELECT r.app_id as appId,resource.app_id as appId,resource.service_id as serviceId,resource.url as url,resource.method as method FROM role r INNER JOIN role_permission rp on r.id=rp.role_id INNER JOIN permission_resource pr ON rp.permission_id=pr.permission_id INNER JOIN resource on resource.id=pr.resource_id where r.code=#{code}")
    fun selectMenuResourceIdByRoleCode(@Param("code") code: String): List<ResourceDto>

    @Select("SELECT r.app_id as appId,resource.app_id as appId,resource.service_id as serviceId,resource.url as url,resource.method as method FROM role r INNER JOIN role_menu rm on r.id=rm.role_id INNER JOIN menu_resource mr ON rm.menu_id=mr.menu_id INNER JOIN resource on resource.id=mr.resource_id where r.code=#{code}")
    fun selectPermissionResourceIdByRoleCode(@Param("code") code: String): List<ResourceDto>

    @Select("SELECT id,app_id as appId,name,value,description,lft,rgt,lvl,sort,fixed,isdel from dict where isdel=0")
    fun selectDict(): List<DictDto>

}
