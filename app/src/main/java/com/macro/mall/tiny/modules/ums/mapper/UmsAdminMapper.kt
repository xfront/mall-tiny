package com.macro.mall.tiny.modules.ums.mapper

import com.github.xfront.ktormplus.KtormMapper
import com.macro.mall.tiny.modules.ums.model.UmsAdmin
import com.macro.mall.tiny.modules.ums.model.UmsAdminRoleRelations
import com.macro.mall.tiny.modules.ums.model.UmsAdmins
import com.macro.mall.tiny.modules.ums.model.UmsRoleResourceRelations
import org.ktorm.dsl.*
import org.springframework.stereotype.Component

/**
 *
 *
 * 后台用户表 Mapper 接口
 *
 *
 * @author macro
 * @since 2020-08-21
 */
@Component
class UmsAdminMapper : KtormMapper<UmsAdmin, UmsAdmins>() {
    /**
     * 获取资源相关用户ID列表
     */
    fun getAdminIdList(resourceId: Long): List<Long> {
        val rrr = UmsRoleResourceRelations.aliased("rrr")
        val arr = UmsAdminRoleRelations.aliased("arr")
        return entities.database.from(rrr)
                .leftJoin(arr, on = arr.roleId eq rrr.roleId)
                .selectDistinct(arr.adminId)
                .where {
                    arr.roleId eq resourceId
                }
                .map { it[arr.adminId] as Long }
                .toList()
    }
}