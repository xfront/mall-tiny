package com.macro.mall.tiny.modules.ums.service.impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.macro.mall.tiny.modules.ums.mapper.UmsResourceMapper
import com.macro.mall.tiny.modules.ums.model.UmsResource
import com.macro.mall.tiny.modules.ums.service.UmsAdminCacheService
import com.macro.mall.tiny.modules.ums.service.UmsResourceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

/**
 * 后台资源管理Service实现类
 * Created by macro on 2020/2/2.
 */
@Service
class UmsResourceServiceImpl : ServiceImpl<UmsResourceMapper, UmsResource>(), UmsResourceService {
    @Autowired
    lateinit var adminCacheService: UmsAdminCacheService
    override fun create(umsResource: UmsResource): Boolean {
        umsResource.createTime = Date()
        return save(umsResource)
    }

    override fun update(id: Long, umsResource: UmsResource): Boolean {
        umsResource.id = id
        val success = updateById(umsResource)
        adminCacheService.delResourceListByResource(id)
        return success
    }

    override fun delete(id: Long): Boolean {
        val success = removeById(id)
        adminCacheService.delResourceListByResource(id)
        return success
    }

    override fun list(
        categoryId: Long?, nameKeyword: String?, urlKeyword: String?, pageSize: Long, pageNum: Long
    ): Page<UmsResource> {
        val page = Page<UmsResource>(pageNum, pageSize)
        val wrapper = QueryWrapper<UmsResource>()

        if (categoryId != null) {
            wrapper.eq("category_id", categoryId)
        }
        if (!nameKeyword.isNullOrBlank()) {
            wrapper.like("name", nameKeyword)
        }
        if (!urlKeyword.isNullOrBlank()) {
            wrapper.like("url", urlKeyword)
        }
        return page(page, wrapper)
    }
}