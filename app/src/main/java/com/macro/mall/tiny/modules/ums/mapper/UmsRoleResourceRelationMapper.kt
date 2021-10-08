package com.macro.mall.tiny.modules.ums.mapper

import com.github.xfront.ktormplus.KtormMapper
import com.macro.mall.tiny.modules.ums.model.UmsRoleResourceRelation
import com.macro.mall.tiny.modules.ums.model.UmsRoleResourceRelations
import org.springframework.stereotype.Component

/**
 *
 *
 * 后台角色资源关系表 Mapper 接口
 *
 *
 * @author macro
 * @since 2020-08-21
 */
@Component
class UmsRoleResourceRelationMapper : KtormMapper<UmsRoleResourceRelation, UmsRoleResourceRelations>()