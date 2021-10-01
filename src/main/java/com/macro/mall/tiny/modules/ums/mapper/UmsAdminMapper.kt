package com.macro.mall.tiny.modules.ums.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.macro.mall.tiny.modules.ums.model.UmsAdmin
import org.apache.ibatis.annotations.Param

/**
 *
 *
 * 后台用户表 Mapper 接口
 *
 *
 * @author macro
 * @since 2020-08-21
 */
interface UmsAdminMapper : BaseMapper<UmsAdmin> {
    /**
     * 获取资源相关用户ID列表
     */
    fun getAdminIdList(@Param("resourceId") resourceId: Long): List<Long>
}