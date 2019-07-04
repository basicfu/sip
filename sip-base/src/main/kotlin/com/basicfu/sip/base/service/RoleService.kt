package com.basicfu.sip.base.service

import com.basicfu.sip.base.mapper.*
import com.basicfu.sip.base.model.po.Menu
import com.basicfu.sip.base.model.po.Permission
import com.basicfu.sip.base.model.po.Role
import com.basicfu.sip.base.model.po.UserRole
import com.basicfu.sip.base.model.vo.RoleVo
import com.basicfu.sip.client.util.UserUtil
import com.basicfu.sip.common.constant.Constant
import com.basicfu.sip.common.enum.Enum
import com.basicfu.sip.common.model.dto.AppDto
import com.basicfu.sip.common.model.dto.ResourceDto
import com.basicfu.sip.common.model.dto.RoleDto
import com.basicfu.sip.common.model.dto.UserDto
import com.basicfu.sip.common.model.po.RoleMenu
import com.basicfu.sip.common.model.po.RolePermission
import com.basicfu.sip.common.util.AppUtil
import com.basicfu.sip.common.util.PermissionUtil
import com.basicfu.sip.core.common.exception.CustomException
import com.basicfu.sip.core.common.mapper.example
import com.basicfu.sip.core.common.mapper.generate
import com.basicfu.sip.core.service.BaseService
import com.basicfu.sip.core.util.RedisUtil
import com.github.pagehelper.PageInfo
import org.apache.commons.lang3.StringUtils
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
    lateinit var menuMapper: MenuMapper
    @Autowired
    lateinit var roleMapper: RoleMapper
    @Autowired
    lateinit var resourceMapper: ResourceMapper
    @Autowired
    lateinit var permissionMapper: PermissionMapper
    @Autowired
    lateinit var menuResourceMapper: MenuResourceMapper
    @Autowired
    lateinit var permissionResourceMapper: PermissionResourceMapper
    @Autowired
    lateinit var appMapper: AppMapper

    fun listRoleByUid(uid: Long): List<String> {
        val roleIds = urMapper.selectByExample(example<UserRole> {
            select(UserRole::roleId)
            andEqualTo(UserRole::userId, uid)
        }).mapNotNull { it.roleId }.toMutableList()
        return if (roleIds.isNotEmpty()) {
            to<RoleDto>(roleMapper.selectByExample(example<Role> {
                andIn(Role::id, roleIds)
            }))
        } else {
            arrayListOf()
        }.map { it.code!! }
    }

    fun listAppRoleByUid(uid: Long): Map<String,List<String>> {
        val roleIds = urMapper.selectByExample(example<UserRole> {
            select(UserRole::roleId)
            andEqualTo(UserRole::userId, uid)
        }).mapNotNull { it.roleId }.toMutableList()
        return if (roleIds.isNotEmpty()) {
            val list=to<RoleDto>(roleMapper.selectByExample(example<Role> {
                andIn(Role::id, roleIds)
            }))
            val map=appMapper.selectByIds(StringUtils.join(list.map { it.appId!! },",")).associateBy({ it.id!! },{it.name!!})
            list.groupBy({ map[it.appId]!! },{it.code!!})
        } else {
            emptyMap()
        }
    }

    fun list(vo: RoleVo): PageInfo<RoleDto> {
        return selectPage(example<Role> {
            orLike {
                name = vo.q
                code = vo.q
            }
            orderByDesc(Role::cdate)
        })
    }

    fun listUserById(id: Long): PageInfo<UserDto> {
        startPage()
        val result = urMapper.selectByExample(example<UserRole> {
            andEqualTo(UserRole::roleId, id)
            orderByDesc(UserRole::cdate)
        })
        val userRolePageInfo = getPageInfo<UserRole>(result)
        val pageInfo = PageInfo<UserDto>()
        pageInfo.total = userRolePageInfo.total
        pageInfo.pageNum = userRolePageInfo.pageNum
        pageInfo.pageSize = userRolePageInfo.pageSize
        val userIds = result.map { it.userId!! }
        if (userIds.isNotEmpty()) {
            val users = UserUtil.listByIds<UserDto>(userIds)
            pageInfo.list = users
        } else {
            pageInfo.list = emptyList()
        }
        return pageInfo
    }

    fun listMenuById(id: Long): List<Long> {
        return rmMapper.selectByExample(example<RoleMenu> {
            andEqualTo(RoleMenu::roleId, id)
        }).map { it.menuId!! }
    }

    fun listPermissionById(id: Long): List<Long> {
        return rpMapper.selectByExample(example<RoleMenu> {
            andEqualTo(RoleMenu::roleId, id)
        }).map { it.permissionId!! }
    }

    fun all(): List<RoleDto> = to(mapper.selectAll())

    fun insert(vo: RoleVo): Int {
        if (mapper.selectCount(generate {
                name = vo.name
            }) != 0) throw CustomException(Enum.EXIST_ROLE_NAME)
        if (mapper.selectCount(generate {
                code = vo.code
            }) != 0) throw CustomException(Enum.EXIST_ROLE_CODE)
        val po = dealInsert(to<Role>(vo))!!
        val count = mapper.insertSelective(po)
        refreshRolePermission()
        return count
    }

    fun insertUser(vo: RoleVo): Int {
        var userIds = vo.userIds!!
        if (UserUtil.listUsernameByIds(userIds).isEmpty()) {
            throw CustomException(Enum.NOT_FOUND_USER_ID)
        }
        val existsUserIds = urMapper.selectByExample(example<UserRole> {
            andEqualTo(UserRole::roleId, vo.id)
            andIn(UserRole::userId, userIds)
        }).map { it.userId }
        userIds = userIds.filter { !existsUserIds.contains(it) }
        if (userIds.isEmpty()) {
            throw CustomException(Enum.EXIST_ADD_DATA)
        }
        val userRoles = arrayListOf<UserRole>()
        userIds.forEach { it ->
            val ur = UserRole()
            ur.roleId = vo.id
            ur.userId = it
            userRoles.add(dealInsert(ur))
        }
        return urMapper.insertList(userRoles)
    }

    fun insertMenu(vo: RoleVo): Int {
        var ids = vo.menuIds!!
        if (menuMapper.selectCountByExample(example<Menu> {
                andIn(Menu::id, ids)
            }) != ids.size) throw CustomException(Enum.NOT_FOUND_MENU)
        val existsMenuIds = rmMapper.selectByExample(example<RoleMenu> {
            andEqualTo(RoleMenu::roleId, vo.id)
            andIn(RoleMenu::menuId, ids)
        }).map { it.menuId }
        ids = ids.filter { !existsMenuIds.contains(it) }
        if (ids.isEmpty()) {
            throw CustomException(Enum.EXIST_ADD_DATA)
        }
        val roleMenus = arrayListOf<RoleMenu>()
        ids.forEach { it ->
            val rm = RoleMenu()
            rm.roleId = vo.id
            rm.menuId = it
            roleMenus.add(dealInsert(rm))
        }
        val count = rmMapper.insertList(roleMenus)
        refreshRolePermission()
        return count
    }

    fun insertPermission(vo: RoleVo): Int {
        var ids = vo.permissionIds!!
        if (permissionMapper.selectCountByExample(example<Permission> {
                andIn(Permission::id, ids)
            }) != ids.size) throw CustomException(Enum.NOT_FOUND_MENU)
        val existsPermissionIds = rpMapper.selectByExample(example<RolePermission> {
            andEqualTo(RolePermission::roleId, vo.id)
            andIn(RolePermission::permissionId, ids)
        }).map { it.permissionId }
        ids = ids.filter { !existsPermissionIds.contains(it) }
        if (ids.isEmpty()) {
            throw CustomException(Enum.EXIST_ADD_DATA)
        }
        val rolePermissions = arrayListOf<RolePermission>()
        ids.forEach { it ->
            val rp = RolePermission()
            rp.roleId = vo.id
            rp.permissionId = it
            rolePermissions.add(dealInsert(rp))
        }
        val count = rpMapper.insertList(rolePermissions)
        refreshRolePermission()
        return count
    }

    fun update(vo: RoleVo): Int {
        val checkName = mapper.selectOne(generate {
            name = vo.name
        })
        if (checkName != null && checkName.id != vo.id) throw CustomException(Enum.EXIST_ROLE_NAME)
        val checkCode = mapper.selectOne(generate {
            code = vo.code
        })
        if (checkCode != null && checkCode.id != vo.id) throw CustomException(Enum.EXIST_ROLE_CODE)
        val po = dealUpdate(to<Role>(vo))
        return mapper.updateByPrimaryKeySelective(po)
    }

    fun delete(ids: List<Long>): Int {
        if (ids.isNotEmpty()) {
            urMapper.deleteByExample(example<UserRole> {
                andIn(UserRole::roleId, ids)
            })
            rpMapper.deleteByExample(example<RolePermission> {
                andIn(RolePermission::roleId, ids)
            })
            rmMapper.deleteByExample(example<RoleMenu> {
                andIn(RoleMenu::roleId, ids)
            })
        }
        val count = deleteByIds(ids)
        refreshRolePermission()
        return count
    }

    fun deleteUser(vo: RoleVo): Int {
        return urMapper.deleteByExample(example<UserRole> {
            andEqualTo(UserRole::roleId, vo.id)
            andIn(UserRole::userId, vo.userIds!!)
        })
    }

    fun deleteMenu(vo: RoleVo): Int {
        val count = rmMapper.deleteByExample(example<RoleMenu> {
            andEqualTo(RoleMenu::roleId, vo.id)
            andIn(RoleMenu::menuId, vo.menuIds!!)
        })
        refreshRolePermission()
        return count
    }

    fun deletePermission(vo: RoleVo): Int {
        val count = rpMapper.deleteByExample(example<RolePermission> {
            andEqualTo(RolePermission::roleId, vo.id)
            andIn(RolePermission::permissionId, vo.permissionIds!!)
        })
        refreshRolePermission()
        return count
    }

    /**
     * 刷新一个用户的权限
     */
    fun refreshRolePermission() {
        val apps = RedisUtil.hGetAll<AppDto>(Constant.Redis.APP).values.toList().map { it!! }
        val roles = to<RoleDto>(mapper.selectAll())
        AppUtil.notCheckApp()
        val resources = to<ResourceDto>(resourceMapper.selectAll())
        val roleMenus = rmMapper.selectAll()
        val rolePermissions = rpMapper.selectAll()
        val menuResources = menuResourceMapper.selectAll()
        val permissionResources = permissionResourceMapper.selectAll()
        PermissionUtil.initRolePermission(
            apps,
            roles,
            resources,
            roleMenus,
            rolePermissions,
            menuResources,
            permissionResources
        )
    }
}
