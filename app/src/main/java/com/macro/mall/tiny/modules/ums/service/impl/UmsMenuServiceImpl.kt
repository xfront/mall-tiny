package com.macro.mall.tiny.modules.ums.service.impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.macro.mall.tiny.modules.ums.dto.UmsMenuNode
import com.macro.mall.tiny.modules.ums.mapper.UmsMenuMapper
import com.macro.mall.tiny.modules.ums.model.UmsMenu
import com.macro.mall.tiny.modules.ums.service.UmsMenuService
import org.springframework.beans.BeanUtils
import org.springframework.stereotype.Service
import java.util.*

/**
 * 后台菜单管理Service实现类
 * Created by macro on 2020/2/2.
 */
@Service
class UmsMenuServiceImpl : ServiceImpl<UmsMenuMapper, UmsMenu>(), UmsMenuService {
    override fun create(umsMenu: UmsMenu): Boolean {
        umsMenu.createTime = Date()
        updateLevel(umsMenu)
        return save(umsMenu)
    }

    /**
     * 修改菜单层级
     */
    private fun updateLevel(umsMenu: UmsMenu) {
        if (umsMenu.parentId == 0L) { //没有父菜单时为一级菜单
            umsMenu.level = 0
        } else { //有父菜单时选择根据父菜单level设置
            val parentMenu = getById(umsMenu.parentId)
            if (parentMenu != null) {
                umsMenu.level = parentMenu.level + 1
            } else {
                umsMenu.level = 0
            }
        }
    }

    override fun update(id: Long, umsMenu: UmsMenu): Boolean {
        umsMenu.id = id
        updateLevel(umsMenu)
        return updateById(umsMenu)
    }

    override fun list(parentId: Long, pageSize: Long, pageNum: Long): Page<UmsMenu> {
        val page = Page<UmsMenu>(pageNum, pageSize)
        val wrapper = QueryWrapper<UmsMenu>()
        wrapper.eq("parent_id", parentId).orderByDesc("sort")
        return page(page, wrapper)
    }

    override fun treeList(): List<UmsMenuNode> {
        val menuList = list()
        return menuList
                .filter { it.parentId == 0L }
                .map { covertMenuNode(it, menuList) }
    }

    override fun updateHidden(id: Long, hidden: Int): Boolean {
        val umsMenu = UmsMenu()
        umsMenu.id = id
        umsMenu.hidden = hidden
        return updateById(umsMenu)
    }

    /**
     * 将UmsMenu转化为UmsMenuNode并设置children属性
     */
    private fun covertMenuNode(menu: UmsMenu, menuList: List<UmsMenu>): UmsMenuNode {
        val node = UmsMenuNode()
        BeanUtils.copyProperties(menu, node)
        val children = menuList
                .filter { it.parentId == menu.id }
                .map { covertMenuNode(it, menuList) }

        node.children = children
        return node
    }
}