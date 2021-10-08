package com.macro.mall.tiny.modules.ums.mapper

import com.github.xfront.ktormplus.KtormMapper
import com.macro.mall.tiny.modules.ums.model.UmsAdminRoleRelations
import com.macro.mall.tiny.modules.ums.model.UmsRole
import com.macro.mall.tiny.modules.ums.model.UmsRoles
import org.ktorm.dsl.*
import org.springframework.stereotype.Component

/**
 *
 *
 * 后台用户角色表 Mapper 接口
 *
 *
 * @author macro
 * @since 2020-08-21
 */
@Component
class UmsRoleMapper : KtormMapper<UmsRole, UmsRoles>() {
    /**
     * 获取用户所有角色
     */
    fun getRoleList(adminId: Long): List<UmsRole> {
        val arr = UmsAdminRoleRelations.aliased("arr")
        val r = UmsRoles.aliased("r")
        return entities.database.from(arr)
                .leftJoin(r, on = arr.roleId eq r.id)
                .select(r.columns)
                .where {
                    arr.adminId eq adminId
                }
                .map { r.createEntity(it, false) }
                .toList()
    }
}