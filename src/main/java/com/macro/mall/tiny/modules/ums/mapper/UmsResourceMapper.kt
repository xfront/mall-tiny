package com.macro.mall.tiny.modules.ums.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.macro.mall.tiny.modules.ums.model.UmsResource
import org.apache.ibatis.annotations.Param

/**
 *
 *
 * 后台资源表 Mapper 接口
 *
 *
 * @author macro
 * @since 2020-08-21
 */
interface UmsResourceMapper : BaseMapper<UmsResource> {
    /**
     * 获取用户所有可访问资源
     */
    fun getResourceList(@Param("adminId") adminId: Long): List<UmsResource>

    /**
     * 根据角色ID获取资源
     */
    fun getResourceListByRoleId(@Param("roleId") roleId: Long): List<UmsResource>
}