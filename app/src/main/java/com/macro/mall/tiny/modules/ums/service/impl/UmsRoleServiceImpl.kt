package com.macro.mall.tiny.modules.ums.service.impl

import com.github.xfront.ktormplus.IPage
import com.github.xfront.ktormplus.Page
import com.github.xfront.ktormplus.ServiceImpl
import com.macro.mall.tiny.modules.ums.mapper.UmsMenuMapper
import com.macro.mall.tiny.modules.ums.mapper.UmsResourceMapper
import com.macro.mall.tiny.modules.ums.mapper.UmsRoleMapper
import com.macro.mall.tiny.modules.ums.model.*
import com.macro.mall.tiny.modules.ums.service.UmsAdminCacheService
import com.macro.mall.tiny.modules.ums.service.UmsRoleMenuRelationService
import com.macro.mall.tiny.modules.ums.service.UmsRoleResourceRelationService
import com.macro.mall.tiny.modules.ums.service.UmsRoleService
import org.ktorm.dsl.combineConditions
import org.ktorm.dsl.eq
import org.ktorm.dsl.like
import org.ktorm.schema.ColumnDeclaring
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

/**
 * 后台角色管理Service实现类
 * Created by macro on 2018/9/30.
 */
@Service
class UmsRoleServiceImpl : ServiceImpl<UmsRoleMapper, UmsRole, UmsRoles>(), UmsRoleService {

    @Autowired
    lateinit var adminCacheService: UmsAdminCacheService

    @Autowired
    lateinit var roleMenuRelationService: UmsRoleMenuRelationService

    @Autowired
    lateinit var roleResourceRelationService: UmsRoleResourceRelationService

    @Autowired
    lateinit var menuMapper: UmsMenuMapper

    @Autowired
    lateinit var resourceMapper: UmsResourceMapper

    override fun create(role: UmsRole): Boolean {
        role.createTime = LocalDateTime.now()
        role.adminCount = 0
        role.sort = 0
        return save(role)
    }

    override fun delete(ids: List<Long>): Boolean {
        val success = removeByIds(ids)
        adminCacheService.delResourceListByRoleIds(ids)
        return success
    }

    override fun list(keyword: String?, pageSize: Int, pageNum: Int): IPage<UmsRole> {
        val page = Page<UmsRole>(pageNum, pageSize)
        return page(page) {
            val cond = mutableListOf<ColumnDeclaring<Boolean>>()
            if (!keyword.isNullOrBlank()) {
                cond += it.name like keyword
            }
            cond.combineConditions()
        }
    }

    override fun getMenuList(adminId: Long): List<UmsMenu> {
        return menuMapper.getMenuList(adminId)
    }

    override fun listMenu(roleId: Long): List<UmsMenu> {
        return menuMapper.getMenuListByRoleId(roleId)
    }

    override fun listResource(roleId: Long): List<UmsResource> {
        return resourceMapper.getResourceListByRoleId(roleId)
    }

    override fun allocMenu(roleId: Long, menuIds: List<Long>): Int { //先删除原有关系
        roleMenuRelationService.remove { it.roleId eq roleId }

        //批量插入新关系
        val relationList = mutableListOf<UmsRoleMenuRelation>()
        for (menuId in menuIds) {
            val relation = UmsRoleMenuRelation()
            relation.roleId = roleId
            relation.menuId = menuId
            relationList.add(relation)
        }
        roleMenuRelationService.saveBatch(relationList)
        return menuIds.size
    }

    override fun allocResource(roleId: Long, resourceIds: List<Long>): Int { //先删除原有关系
        roleResourceRelationService.remove { it.roleId eq roleId }

        //批量插入新关系
        val relationList = mutableListOf<UmsRoleResourceRelation>()
        for (resourceId in resourceIds) {
            val relation = UmsRoleResourceRelation()
            relation.roleId = roleId
            relation.resourceId = resourceId
            relationList.add(relation)
        }
        roleResourceRelationService.saveBatch(relationList)
        adminCacheService.delResourceListByRole(roleId)
        return resourceIds.size
    }
}