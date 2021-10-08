package com.macro.mall.tiny.modules.ums.service

import com.github.xfront.ktormplus.IService
import com.macro.mall.tiny.modules.ums.model.UmsResourceCategory
import com.macro.mall.tiny.modules.ums.model.UmsResourceCategorys

/**
 * 后台资源分类管理Service
 * Created by macro on 2020/2/5.
 */
interface UmsResourceCategoryService : IService<UmsResourceCategory, UmsResourceCategorys> {
    /**
     * 获取所有资源分类
     */
    fun listAll(): List<UmsResourceCategory>

    /**
     * 创建资源分类
     */
    fun create(umsResourceCategory: UmsResourceCategory): Boolean
}