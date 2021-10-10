package com.macro.mall.tiny.modules.ums.service.impl

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.macro.mall.tiny.modules.ums.mapper.UmsMenuMapper
import com.macro.mall.tiny.modules.ums.mapper.UmsResourceMapper
import com.macro.mall.tiny.modules.ums.mapper.UmsRoleMapper
import com.macro.mall.tiny.modules.ums.model.*
import com.macro.mall.tiny.modules.ums.service.UmsAdminCacheService
import com.macro.mall.tiny.modules.ums.service.UmsRoleMenuRelationService
import com.macro.mall.tiny.modules.ums.service.UmsRoleResourceRelationService
import com.macro.mall.tiny.modules.ums.service.UmsRoleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

/**
 * 后台角色管理Service实现类
 * Created by macro on 2018/9/30.
 */
@Service
class UmsRoleServiceImpl : ServiceImpl<UmsRoleMapper, UmsRole>(), UmsRoleService {
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
        role.createTime = Date()
        role.adminCount = 0
        role.sort = 0
        return save(role)
    }

    override fun delete(ids: List<Long>): Boolean {
        val success = removeByIds(ids)
        adminCacheService.delResourceListByRoleIds(ids)
        return success
    }

    override fun list(keyword: String?, pageSize: Long, pageNum: Long): Page<UmsRole> {
        val page = Page<UmsRole>(pageNum, pageSize)
        val wrapper = KtQueryWrapper(UmsRole::class.java)
        if (!keyword.isNullOrBlank()) {
            wrapper.like(UmsRole::name, keyword)
        }
        return page(page, wrapper)
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

    override fun allocMenu(roleId: Long, menuIds: List<Long>): Int {
        //先删除原有关系
        roleMenuRelationService.ktUpdate()
                .eq(UmsRoleMenuRelation::roleId, roleId)
                .remove()

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

    override fun allocResource(roleId: Long, resourceIds: List<Long>): Int {
        //先删除原有关系
        roleResourceRelationService.ktUpdate()
                .eq(UmsRoleResourceRelation::roleId, roleId)
                .remove()

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