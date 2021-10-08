package com.macro.mall.tiny.modules.ums.mapper

import com.github.xfront.ktormplus.KtormMapper
import com.macro.mall.tiny.modules.ums.model.UmsRoleMenuRelation
import com.macro.mall.tiny.modules.ums.model.UmsRoleMenuRelations
import org.springframework.stereotype.Component

/**
 *
 *
 * 后台角色菜单关系表 Mapper 接口
 *
 *
 * @author macro
 * @since 2020-08-21
 */
@Component
class UmsRoleMenuRelationMapper : KtormMapper<UmsRoleMenuRelation, UmsRoleMenuRelations>()