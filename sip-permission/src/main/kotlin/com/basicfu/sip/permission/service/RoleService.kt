package com.basicfu.sip.permission.service

import com.alibaba.fastjson.JSONObject
import com.basicfu.sip.core.common.Constant
import com.basicfu.sip.core.common.exception.CustomException
import com.basicfu.sip.core.common.mapper.example
import com.basicfu.sip.core.common.mapper.generate
import com.basicfu.sip.core.model.dto.MenuDto
import com.basicfu.sip.core.service.BaseService
import com.basicfu.sip.core.util.AppUtil
import com.basicfu.sip.core.util.MenuUtil
import com.basicfu.sip.permission.common.Enum
import com.basicfu.sip.permission.mapper.*
import com.basicfu.sip.permission.model.dto.RoleDto
import com.basicfu.sip.permission.model.po.*
import com.basicfu.sip.permission.model.vo.RoleVo
import com.github.pagehelper.PageInfo
import org.apache.commons.lang.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author basicfu
 * @date 2018/7/9
 */
@Service
class RoleService : BaseService<RoleMapper, Role>() {

    @Autowired
    lateinit var urMapper: UserRoleMapper
    @Autowired
    lateinit var rpMapper: RolePermissionMapper
    @Autowired
    lateinit var rmMapper: RoleMenuMapper
    @Autowired
    lateinit var mrMapper: MenuResourceMapper
    @Autowired
    lateinit var prMapper: PermissionResourceMapper
    @Autowired
    lateinit var menuMapper: MenuMapper
    @Autowired
    lateinit var roleMapper: RoleMapper
    @Autowired
    lateinit var permissionMapper: PermissionMapper
    @Autowired
    lateinit var resourceMapper: ResourceMapper

    fun getPermissionByUid(uid: Long): JSONObject {
        val roleIds = urMapper.selectByExample(example<UserRole> {
            select(UserRole::roleId, UserRole::id)
            andEqualTo(UserRole::userId, uid)
        }).mapNotNull { it.roleId }.toMutableList()
        //登录用户包含未登录用户的权限
        val noLoginRoleId = mapper.selectOneByExample(example<Role> {
            select(Role::id)
            andEqualTo(Role::code, Constant.System.GUEST)
        }).id
        noLoginRoleId?.let { roleIds.add(it) }
        val menuIds = if (roleIds.isEmpty()) {
            emptyList()
        } else {
            rmMapper.selectByExample(example<RoleMenu> {
                select(RoleMenu::menuId)
                andIn(RoleMenu::roleId, roleIds)
            }).mapNotNull { it.menuId }
        }
        val menuResourceIds = if (menuIds.isEmpty()) {
            emptyList()
        } else {
            mrMapper.selectByExample(example<MenuResource> {
                select(MenuResource::resourceId)
                andIn(MenuResource::menuId, menuIds)
            }).mapNotNull { it.resourceId }
        }
        val permissionIds = if (roleIds.isEmpty()) {
            emptyList()
        } else {
            rpMapper.selectByExample(example<RolePermission> {
                select(RolePermission::permissionId)
                andIn(RolePermission::roleId, roleIds)
            }).mapNotNull { it.permissionId }
        }
        val permissionResourceIds = if (permissionIds.isEmpty()) {
            emptyList()
        } else {
            prMapper.selectByExample(example<PermissionResource> {
                select(PermissionResource::resourceId)
                andIn(PermissionResource::permissionId, permissionIds)
            }).mapNotNull { it.resourceId }
        }
        roleIds.remove(noLoginRoleId)
        val roles=if(roleIds.isNotEmpty()){
            to<RoleDto>(roleMapper.selectByExample(example<Role> {
                andIn(Role::id, roleIds)
            }))
        }else{
            arrayListOf()
        }
        val menus = if(menuIds.isNotEmpty()){
            MenuUtil.recursive(null, to(menuMapper.selectByExample(example<Menu> {
                andIn(Menu::id, menuIds)
            })))
        }else{
            arrayListOf()
        }
        val permissions = if(permissionIds.isEmpty()){
            to<MenuDto>(permissionMapper.selectByExample(example<Permission> {
                andIn(Permission::id, permissionIds)
            }))
        }else{
            arrayListOf()
        }
        val resourceIds = arrayListOf<Long>()
        resourceIds.addAll(menuResourceIds)
        resourceIds.addAll(permissionResourceIds)
        //其他应用可以有系统的应用resource，所以此处不使用appId
        AppUtil.appNotCheck()
        val resources = resourceMapper.selectByIds(StringUtils.join(resourceIds.distinct(), ","))
        val result = JSONObject()
        result["roles"] = roles
        result["menus"] = menus
        result["permissions"] = permissions
        result["resources"] = resources
        return result
    }

    fun list(vo: RoleVo): PageInfo<RoleDto> {
        return selectPage(example<Role> {
            andLike(Role::name, vo.name)
        })
    }

    fun all(): List<RoleDto> = to(mapper.selectAll())

    fun insert(vo: RoleVo): Int {
        if (mapper.selectCount(generate {
                name = vo.name
            }) != 0) throw CustomException(Enum.Role.EXIST_NAME)
        val po = dealInsert(to<Role>(vo))
        return mapper.insertSelective(po)
    }

    fun insertUser(vo: RoleVo): Int {
        var ids = vo.userIds!!
        //暂时不验证用户ID是否存在
//        if(menuService.mapper.selectCountByExample(example<Menu> {
//                andIn(Menu::id, ids)
//            })!=ids.size)throw CustomException(Enum.Role.MENU_NOT_FOUND)
        val existsUserIds = urMapper.selectByExample(example<UserRole> {
            andEqualTo(UserRole::roleId, vo.id)
            andIn(UserRole::userId, ids)
        }).map { it.userId }
        ids = ids.filter { !existsUserIds.contains(it) }
        val userRoles = arrayListOf<UserRole>()
        ids.forEach { it ->
            val ur = UserRole()
            ur.roleId = vo.id
            ur.userId = it
            userRoles.add(ur)
        }
        return urMapper.insertList(userRoles)
    }

    fun insertMenu(vo: RoleVo): Int {
        var ids = vo.menuIds!!
        if (menuMapper.selectCountByExample(example<Menu> {
                andIn(Menu::id, ids)
            }) != ids.size) throw CustomException(Enum.Role.MENU_NOT_FOUND)
        val existsMenuIds = rmMapper.selectByExample(example<RoleMenu> {
            andEqualTo(RoleMenu::roleId, vo.id)
            andIn(RoleMenu::menuId, ids)
        }).map { it.menuId }
        ids = ids.filter { !existsMenuIds.contains(it) }
        val roleMenus = arrayListOf<RoleMenu>()
        ids.forEach { it ->
            val rm = RoleMenu()
            rm.roleId = vo.id
            rm.menuId = it
            roleMenus.add(rm)
        }
        return rmMapper.insertList(roleMenus)
    }

    fun insertPermission(vo: RoleVo): Int {
        var ids = vo.permissionIds!!
        if (permissionMapper.selectCountByExample(example<Permission> {
                andIn(Permission::id, ids)
            }) != ids.size) throw CustomException(Enum.Role.MENU_NOT_FOUND)
        val existsPermissionIds = rpMapper.selectByExample(example<RolePermission> {
            andEqualTo(RolePermission::roleId, vo.id)
            andIn(RolePermission::permissionId, ids)
        }).map { it.permissionId }
        ids = ids.filter { !existsPermissionIds.contains(it) }
        val rolePermissions = arrayListOf<RolePermission>()
        ids.forEach { it ->
            val rp = RolePermission()
            rp.roleId = vo.id
            rp.permissionId = it
            rolePermissions.add(rp)
        }
        return rpMapper.insertList(rolePermissions)
    }

    fun update(vo: RoleVo): Int {
        val check = mapper.selectOne(generate {
            name = vo.name
            enalbe = vo.enalbe
        })
        if (check != null && check.id != vo.id) throw CustomException(Enum.Role.EXIST_NAME)
        val po = dealUpdate(to<Role>(vo))
        return mapper.updateByPrimaryKeySelective(po)
    }

    fun delete(ids: List<Long>): Int {
        if (ids.isNotEmpty()) {
            rpMapper.deleteByIds(StringUtils.join(ids, ","))
            rmMapper.deleteByIds(StringUtils.join(ids, ","))
        }
        return deleteByIds(ids)
    }

    fun deleteUser(vo: RoleVo): Int {
        return urMapper.deleteByExample(example<UserRole> {
            andEqualTo(UserRole::roleId, vo.id)
            andIn(UserRole::userId, vo.userIds!!)
        })
    }

    fun deleteMenu(vo: RoleVo): Int {
        return rmMapper.deleteByExample(example<RoleMenu> {
            andEqualTo(RoleMenu::roleId, vo.id)
            andIn(RoleMenu::menuId, vo.menuIds!!)
        })
    }

    fun deletePermission(vo: RoleVo): Int {
        return rpMapper.deleteByExample(example<RolePermission> {
            andEqualTo(RolePermission::roleId, vo.id)
            andIn(RolePermission::permissionId, vo.permissionIds!!)
        })
    }
}
