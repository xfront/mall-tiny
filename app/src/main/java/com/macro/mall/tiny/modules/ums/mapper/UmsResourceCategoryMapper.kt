package com.macro.mall.tiny.modules.ums.mapper

import com.github.xfront.ktormplus.KtormMapper
import com.macro.mall.tiny.modules.ums.model.UmsResourceCategory
import com.macro.mall.tiny.modules.ums.model.UmsResourceCategorys
import org.springframework.stereotype.Component

/**
 *
 *
 * 资源分类表 Mapper 接口
 *
 *
 * @author macro
 * @since 2020-08-21
 */
@Component
class UmsResourceCategoryMapper : KtormMapper<UmsResourceCategory, UmsResourceCategorys>()