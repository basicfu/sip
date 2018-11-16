package com.basicfu.sip.getway.mapper

import com.basicfu.sip.common.model.dto.*
import com.basicfu.sip.common.model.po.MenuResource
import com.basicfu.sip.common.model.po.PermissionResource
import com.basicfu.sip.common.model.po.RoleMenu
import com.basicfu.sip.common.model.po.RolePermission
import org.apache.ibatis.annotations.Mapper
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

    /**
     * 只处理启用的role
     */
    @Select("select id,app_id as appId,name,code from role where enable=1")
    fun selectRole(): List<RoleDto>

    @Select("select id,app_id as appId,service_id as serviceId,url,method,name from resource")
    fun selectResource(): List<ResourceDto>

    @Select("select id,app_id as appId,role_id as roleId,menu_id as menuId from role_menu")
    fun selectRoleMenu(): List<RoleMenu>

    @Select("select id,app_id as appId,role_id as roleId,permission_id as permissionId from role_permission")
    fun selectRolePermission(): List<RolePermission>

    @Select("select id,app_id as appId,menu_id as menuId,resource_id as resourceId from menu_resource")
    fun selectMenuResource(): List<MenuResource>

    @Select("select id,app_id as appId,permission_id as permissionId,resource_id as resourceId from permission_resource")
    fun selectPermissionResource(): List<PermissionResource>
}
