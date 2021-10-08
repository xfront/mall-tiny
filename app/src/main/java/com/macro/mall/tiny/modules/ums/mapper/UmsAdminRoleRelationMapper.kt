package com.macro.mall.tiny.modules.ums.mapper

import com.github.xfront.ktormplus.KtormMapper
import com.macro.mall.tiny.modules.ums.model.UmsAdminRoleRelation
import com.macro.mall.tiny.modules.ums.model.UmsAdminRoleRelations
import org.springframework.stereotype.Component

/**
 *
 *
 * 后台用户和角色关系表 Mapper 接口
 *
 *
 * @author macro
 * @since 2020-08-21
 */
@Component
class UmsAdminRoleRelationMapper : KtormMapper<UmsAdminRoleRelation, UmsAdminRoleRelations>()