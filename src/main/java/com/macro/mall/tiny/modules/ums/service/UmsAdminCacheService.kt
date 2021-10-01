package com.macro.mall.tiny.modules.ums.service

import com.macro.mall.tiny.modules.ums.model.UmsAdmin
import com.macro.mall.tiny.modules.ums.model.UmsResource

/**
 * 后台用户缓存管理Service
 * Created by macro on 2020/3/13.
 */
interface UmsAdminCacheService {
    /**
     * 删除后台用户缓存
     */
    fun delAdmin(adminId: Long)

    /**
     * 删除后台用户资源列表缓存
     */
    fun delResourceList(adminId: Long)

    /**
     * 当角色相关资源信息改变时删除相关后台用户缓存
     */
    fun delResourceListByRole(roleId: Long)

    /**
     * 当角色相关资源信息改变时删除相关后台用户缓存
     */
    fun delResourceListByRoleIds(roleIds: List<Long>)

    /**
     * 当资源信息改变时，删除资源项目后台用户缓存
     */
    fun delResourceListByResource(resourceId: Long)

    /**
     * 获取缓存后台用户信息
     */
    fun getAdmin(username: String): UmsAdmin?

    /**
     * 设置缓存后台用户信息
     */
    fun setAdmin(admin: UmsAdmin)

    /**
     * 获取缓存后台用户资源列表
     */
    fun getResourceList(adminId: Long): List<UmsResource>?

    /**
     * 设置后台后台用户资源列表
     */
    fun setResourceList(adminId: Long, resourceList: List<UmsResource>)
}