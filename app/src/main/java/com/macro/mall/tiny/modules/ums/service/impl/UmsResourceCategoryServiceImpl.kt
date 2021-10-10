package com.macro.mall.tiny.modules.ums.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.macro.mall.tiny.modules.ums.mapper.UmsResourceCategoryMapper
import com.macro.mall.tiny.modules.ums.model.UmsResourceCategory
import com.macro.mall.tiny.modules.ums.service.UmsResourceCategoryService
import org.springframework.stereotype.Service
import java.util.*

/**
 * 后台资源分类管理Service实现类
 * Created by macro on 2020/2/5.
 */
@Service
class UmsResourceCategoryServiceImpl : ServiceImpl<UmsResourceCategoryMapper, UmsResourceCategory>(), UmsResourceCategoryService {
    override fun listAll(): List<UmsResourceCategory> {
        return ktQuery().orderByAsc(UmsResourceCategory::sort).list()
    }

    override fun create(umsResourceCategory: UmsResourceCategory): Boolean {
        umsResourceCategory.createTime = Date()
        return save(umsResourceCategory)
    }
}