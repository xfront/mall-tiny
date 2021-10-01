package com.macro.mall.tiny.modules.ums.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.macro.mall.tiny.modules.ums.model.UmsMenu
import org.apache.ibatis.annotations.Param

/**
 *
 *
 * 后台菜单表 Mapper 接口
 *
 *
 * @author macro
 * @since 2020-08-21
 */
interface UmsMenuMapper : BaseMapper<UmsMenu> {
    /**
     * 根据后台用户ID获取菜单
     */
    fun getMenuList(@Param("adminId") adminId: Long): List<UmsMenu>

    /**
     * 根据角色ID获取菜单
     */
    fun getMenuListByRoleId(@Param("roleId") roleId: Long): List<UmsMenu>
}