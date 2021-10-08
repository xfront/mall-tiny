package com.macro.mall.tiny.modules.ums.mapper

import com.github.xfront.ktormplus.KtormMapper
import com.macro.mall.tiny.modules.ums.model.*
import org.ktorm.dsl.*
import org.springframework.stereotype.Component

/**
 *
 *
 * 后台菜单表 Mapper 接口
 *
 *
 * @author macro
 * @since 2020-08-21
 */
@Component
class UmsMenuMapper : KtormMapper<UmsMenu, UmsMenus>() {
    /**
     * 根据后台用户ID获取菜单
     */
    fun getMenuList(adminId: Long): List<UmsMenu> {
        val arr = UmsAdminRoleRelations.aliased("arr")
        val r = UmsRoles.aliased("r")
        val rmr = UmsRoleMenuRelations.aliased("rmr")
        val m = UmsMenus.aliased("m")
        return entities.database.from(arr)
                .leftJoin(r, on = arr.roleId eq r.id)
                .leftJoin(rmr, on = r.id eq rmr.roleId)
                .leftJoin(m, on = rmr.menuId eq m.id)
                .select(m.columns)
                .whereWithConditions {
                    it += arr.adminId eq adminId
                    it += m.id.isNotNull()
                }
                .groupBy(m.id)
                .map { m.createEntity(it, false) }
                .toList()
    }

    /**
     * 根据角色ID获取菜单
     */
    fun getMenuListByRoleId(roleId: Long): List<UmsMenu> {
        val rmr = UmsRoleMenuRelations.aliased("rmr")
        val m = UmsMenus.aliased("m")
        return entities.database.from(rmr)
                .leftJoin(m, on = rmr.menuId eq m.id)
                .select(m.columns)
                .whereWithConditions {
                    it += rmr.roleId eq roleId
                    it += m.id.isNotNull()
                }
                .groupBy(m.id)
                .map { m.createEntity(it, false) }
                .toList()
    }
}