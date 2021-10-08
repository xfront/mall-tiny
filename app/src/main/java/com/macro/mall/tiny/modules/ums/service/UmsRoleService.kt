package com.macro.mall.tiny.modules.ums.service

import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.IService
import com.macro.mall.tiny.modules.ums.model.UmsMenu
import com.macro.mall.tiny.modules.ums.model.UmsResource
import com.macro.mall.tiny.modules.ums.model.UmsRole
import org.springframework.transaction.annotation.Transactional

/**
 * 后台角色管理Service
 * Created by macro on 2018/9/30.
 */
interface UmsRoleService : IService<UmsRole?> {
    /**
     * 添加角色
     */
    fun create(role: UmsRole): Boolean

    /**
     * 批量删除角色
     */
    fun delete(ids: List<Long>): Boolean

    /**
     * 分页获取角色列表
     */
    fun list(keyword: String?, pageSize: Long, pageNum: Long): Page<UmsRole>

    /**
     * 根据管理员ID获取对应菜单
     */
    fun getMenuList(adminId: Long): List<UmsMenu>

    /**
     * 获取角色相关菜单
     */
    fun listMenu(roleId: Long): List<UmsMenu>

    /**
     * 获取角色相关资源
     */
    fun listResource(roleId: Long): List<UmsResource>

    /**
     * 给角色分配菜单
     */
    @Transactional
    fun allocMenu(roleId: Long, menuIds: List<Long>): Int

    /**
     * 给角色分配资源
     */
    @Transactional
    fun allocResource(roleId: Long, resourceIds: List<Long>): Int
}