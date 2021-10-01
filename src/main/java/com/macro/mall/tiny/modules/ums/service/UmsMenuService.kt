package com.macro.mall.tiny.modules.ums.service

import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.IService
import com.macro.mall.tiny.modules.ums.dto.UmsMenuNode
import com.macro.mall.tiny.modules.ums.model.UmsMenu

/**
 * 后台菜单管理Service
 * Created by macro on 2020/2/2.
 */
interface UmsMenuService : IService<UmsMenu> {
    /**
     * 创建后台菜单
     */
    fun create(umsMenu: UmsMenu): Boolean

    /**
     * 修改后台菜单
     */
    fun update(id: Long, umsMenu: UmsMenu): Boolean

    /**
     * 分页查询后台菜单
     */
    fun list(parentId: Long, pageSize: Long, pageNum: Long): Page<UmsMenu>

    /**
     * 树形结构返回所有菜单列表
     */
    fun treeList(): List<UmsMenuNode>

    /**
     * 修改菜单显示状态
     */
    fun updateHidden(id: Long, hidden: Int): Boolean
}