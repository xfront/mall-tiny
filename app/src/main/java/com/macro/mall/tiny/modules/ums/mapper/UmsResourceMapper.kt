package com.macro.mall.tiny.modules.ums.mapper

import com.github.xfront.ktormplus.KtormMapper
import com.macro.mall.tiny.modules.ums.model.*
import org.ktorm.dsl.*
import org.springframework.stereotype.Component

/**
 *
 *
 * 后台资源表 Mapper 接口
 *
 *
 * @author macro
 * @since 2020-08-21
 */
@Component
class UmsResourceMapper : KtormMapper<UmsResource, UmsResources>() {
    /**
     * 获取用户所有可访问资源
     */
    fun getResourceList(adminId: Long): List<UmsResource> {
        val arr = UmsAdminRoleRelations.aliased("arr")
        val r = UmsRoles.aliased("r")
        val rrr = UmsRoleResourceRelations.aliased("rrr")
        val ur = UmsResources.aliased("ur")
        return entities.database.from(arr)
                .leftJoin(r, on = arr.roleId eq r.id)
                .leftJoin(rrr, on = rrr.roleId eq r.id)
                .leftJoin(ur, on = rrr.resourceId eq ur.id)
                .select(ur.columns)
                .whereWithConditions {
                    it += arr.adminId eq adminId
                    it += ur.id.isNotNull()
                }
                .groupBy(ur.id)
                .map { ur.createEntity(it, false) }
                .toList()
    }

    /**
     * 根据角色ID获取资源
     */
    fun getResourceListByRoleId(roleId: Long): List<UmsResource> {
        val rrr = UmsRoleResourceRelations.aliased("rrr")
        val r = UmsResources.aliased("r")
        return entities.database.from(rrr)
                .leftJoin(r, on = rrr.resourceId eq r.id)
                .select(r.columns)
                .whereWithConditions {
                    it += rrr.roleId eq roleId
                    it += r.id.isNotNull()
                }
                .groupBy(r.id)
                .map { r.createEntity(it, false) }
                .toList()
    }
}