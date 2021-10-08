package com.macro.mall.tiny.modules.ums.service.impl

import com.github.xfront.ktormplus.ServiceImpl
import com.macro.mall.tiny.modules.ums.mapper.UmsResourceCategoryMapper
import com.macro.mall.tiny.modules.ums.model.UmsResourceCategory
import com.macro.mall.tiny.modules.ums.model.UmsResourceCategorys
import com.macro.mall.tiny.modules.ums.service.UmsResourceCategoryService
import org.ktorm.entity.sortedBy
import org.ktorm.entity.toList
import org.springframework.stereotype.Service
import java.time.LocalDateTime

/**
 * 后台资源分类管理Service实现类
 * Created by macro on 2020/2/5.
 */
@Service
class UmsResourceCategoryServiceImpl : ServiceImpl<UmsResourceCategoryMapper, UmsResourceCategory, UmsResourceCategorys>(),
    UmsResourceCategoryService {
    override fun listAll(): List<UmsResourceCategory> {
        return mapper.entities
                .sortedBy { it.sort }
                .toList()
    }

    override fun create(umsResourceCategory: UmsResourceCategory): Boolean {
        umsResourceCategory.createTime = LocalDateTime.now()
        return save(umsResourceCategory)
    }
}