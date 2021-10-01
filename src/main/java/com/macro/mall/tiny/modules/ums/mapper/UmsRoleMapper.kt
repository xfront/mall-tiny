package com.macro.mall.tiny.modules.ums.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.macro.mall.tiny.modules.ums.model.UmsRole
import org.apache.ibatis.annotations.Param

/**
 *
 *
 * 后台用户角色表 Mapper 接口
 *
 *
 * @author macro
 * @since 2020-08-21
 */
interface UmsRoleMapper : BaseMapper<UmsRole> {
    /**
     * 获取用户所有角色
     */
    fun getRoleList(@Param("adminId") adminId: Long): List<UmsRole>
}