package com.basicfu.sip.permission.service

import com.basicfu.sip.core.common.mapper.example
import com.basicfu.sip.core.common.mapper.generate
import com.basicfu.sip.core.service.BaseService
import com.basicfu.sip.permission.mapper.RoleMapper
import com.basicfu.sip.permission.mapper.UserRoleMapper
import com.basicfu.sip.permission.model.po.Role
import com.basicfu.sip.permission.model.po.UserRole
import org.apache.commons.lang.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author basicfu
 * @date 2018/9/25
 */
@Service
class UserService : BaseService<RoleMapper, Role>() {

    @Autowired
    lateinit var urMapper: UserRoleMapper

    fun updateRole(id: Long, roleIds: List<Long>): Int {
        /**
         * 此处未做用户是否存在校验，当添加用户和角色时用户数据未提交所以查不到用户,实现方式:
         * 1.针对此方法设置事物隔离级别为isolation=Isolation.READ_UNCOMMITTED，但目前会出现2个事物bean name暂时未研究解决方案
         * 2.添加方法和更新方法区分开，不太想分开2个方法实现
         */
//        if (UserUtil.listUsernameByIds(listOf(id)).isEmpty()) {
//            throw CustomException(Enum.User.USER_NOT_FOUND)
//        }
        val existsRoleIds = urMapper.selectByExample(example<UserRole> {
            andEqualTo(UserRole::userId, id)
        }).map { it.roleId }
        val insertIds = roleIds.filter { !existsRoleIds.contains(it) }
        val deleteIds = existsRoleIds.filter { !roleIds.contains(it) }
        if (insertIds.isNotEmpty()) {
            urMapper.insertList(insertIds.map {
                generate<UserRole> {
                    roleId = it
                    userId = id
                }
            })
        }
        if (deleteIds.isNotEmpty()) {
            urMapper.deleteByIds(StringUtils.join(deleteIds, ","))
        }
        return roleIds.size
    }
}
