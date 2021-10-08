package com.macro.mall.tiny.modules.ums.service.impl

import com.github.xfront.ktormplus.ServiceImpl
import com.macro.mall.tiny.modules.ums.mapper.UmsRoleResourceRelationMapper
import com.macro.mall.tiny.modules.ums.model.UmsRoleResourceRelation
import com.macro.mall.tiny.modules.ums.model.UmsRoleResourceRelations
import com.macro.mall.tiny.modules.ums.service.UmsRoleResourceRelationService
import org.springframework.stereotype.Service

/**
 * 角色资源关系管理Service实现类
 * Created by macro on 2020/8/21.
 */
@Service
class UmsRoleResourceRelationServiceImpl :
    ServiceImpl<UmsRoleResourceRelationMapper, UmsRoleResourceRelation, UmsRoleResourceRelations>(),
    UmsRoleResourceRelationService