package com.macro.mall.tiny.modules.ums.service

import com.github.xfront.ktormplus.IPage
import com.github.xfront.ktormplus.IService
import com.macro.mall.tiny.modules.ums.model.UmsMenu
import com.macro.mall.tiny.modules.ums.model.UmsMenus

/**
 * 后台菜单管理Service
 * Created by macro on 2020/2/2.
 */
interface UmsMenuService : IService<UmsMenu, UmsMenus> {
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
    fun list(parentId: Long, pageSize: Int, pageNum: Int): IPage<UmsMenu>

    /**
     * 树形结构返回所有菜单列表
     */
    fun treeList(): List<UmsMenu>

    /**
     * 修改菜单显示状态
     */
    fun updateHidden(id: Long, hidden: Int): Boolean
}