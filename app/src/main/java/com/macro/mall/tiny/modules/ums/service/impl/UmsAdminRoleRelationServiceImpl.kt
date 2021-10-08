package com.macro.mall.tiny.modules.ums.service.impl

import com.github.xfront.ktormplus.ServiceImpl
import com.macro.mall.tiny.modules.ums.mapper.UmsAdminRoleRelationMapper
import com.macro.mall.tiny.modules.ums.model.UmsAdminRoleRelation
import com.macro.mall.tiny.modules.ums.model.UmsAdminRoleRelations
import com.macro.mall.tiny.modules.ums.service.UmsAdminRoleRelationService
import org.springframework.stereotype.Service

/**
 * 管理员角色关系管理Service实现类
 * Created by macro on 2020/8/21.
 */
@Service
class UmsAdminRoleRelationServiceImpl : ServiceImpl<UmsAdminRoleRelationMapper, UmsAdminRoleRelation, UmsAdminRoleRelations>(),
    UmsAdminRoleRelationService