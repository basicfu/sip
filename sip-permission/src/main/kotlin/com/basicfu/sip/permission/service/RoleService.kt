package com.basicfu.sip.permission.service

import com.alibaba.fastjson.JSONObject
import com.basicfu.sip.core.exception.CustomException
import com.basicfu.sip.core.mapper.example
import com.basicfu.sip.core.mapper.generate
import com.basicfu.sip.core.service.BaseService
import com.basicfu.sip.permission.common.Enum
import com.basicfu.sip.permission.mapper.*
import com.basicfu.sip.permission.model.dto.RoleDto
import com.basicfu.sip.permission.model.po.*
import com.basicfu.sip.permission.model.vo.RoleVo
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
    lateinit var menuService: MenuService

    fun getPermissionByUid(uid: Long): JSONObject {
        val roleIds = urMapper.selectByExample(example<UserRole> {
            select(UserRole::roleId)
            andEqualTo(UserRole::userId, uid)
        }).mapNotNull { it.roleId }
        val menuIds = if (roleIds.isEmpty()) {
            emptyList()
        } else {
            rmMapper.selectByExample(example<RoleMenu> {
                select(RoleMenu::menuId)
                andIn(RoleMenu::menuId, roleIds)
            }).mapNotNull { it.menuId }
        }
        val menuResourceIds = if (menuIds.isEmpty()) {
            emptyList()
        } else {
            mrMapper.selectByExample(example<MenuResource> {
                select(MenuResource::resourceId)
                andIn(MenuResource::resourceId, roleIds)
            }).mapNotNull { it.resourceId }
        }
        val permissionIds = if (roleIds.isEmpty()) {
            emptyList()
        } else {
            rpMapper.selectByExample(example<RolePermission> {
                select(RolePermission::permissionId)
                andIn(RolePermission::permissionId, roleIds)
            }).mapNotNull { it.permissionId }
        }
        val permissionResourceIds = if (permissionIds.isEmpty()) {
            emptyList()
        } else {
            prMapper.selectByExample(example<PermissionResource> {
                select(PermissionResource::resourceId)
                andIn(PermissionResource::resourceId, roleIds)
            }).mapNotNull { it.resourceId }
        }
        val resourceIds = arrayListOf<Long>()
        resourceIds.addAll(menuResourceIds)
        resourceIds.addAll(permissionResourceIds)
        val result = JSONObject()
        result["roleIds"] = roleIds
        result["menuIds"] = menuIds
        result["permissionIds"] = permissionIds
        result["resourceIds"] = resourceIds
        return result
    }

//    fun list(vo: RoleVo): PageInfo<RoleDto> {
//        return selectPage(example<Role> {
//            andLike(Role::name, vo.name)
//        })
//    }

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
        if (menuService.mapper.selectCountByExample(example<Menu> {
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

    fun deleteMenu(vo: RoleVo): Int {
        return rmMapper.deleteByExample(example<RoleMenu> {
            andEqualTo(RoleMenu::roleId, vo.id)
            andIn(RoleMenu::menuId, vo.menuIds!!)
        })
    }
}
