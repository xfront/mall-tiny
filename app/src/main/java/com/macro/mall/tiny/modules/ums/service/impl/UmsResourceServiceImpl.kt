package com.macro.mall.tiny.modules.ums.service.impl

import com.github.xfront.ktormplus.*
import com.macro.mall.tiny.modules.ums.mapper.UmsResourceMapper
import com.macro.mall.tiny.modules.ums.model.UmsResource
import com.macro.mall.tiny.modules.ums.model.UmsResources
import com.macro.mall.tiny.modules.ums.service.UmsAdminCacheService
import com.macro.mall.tiny.modules.ums.service.UmsResourceService
import org.ktorm.dsl.and
import org.ktorm.dsl.combineConditions
import org.ktorm.dsl.eq
import org.ktorm.dsl.like
import org.ktorm.schema.ColumnDeclaring
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

/**
 * 后台资源管理Service实现类
 * Created by macro on 2020/2/2.
 */
@Service
class UmsResourceServiceImpl : ServiceImpl<UmsResourceMapper, UmsResource, UmsResources>(), UmsResourceService {
    @Autowired
    lateinit var adminCacheService: UmsAdminCacheService
    override fun create(umsResource: UmsResource): Boolean {
        umsResource.createTime = LocalDateTime.now()
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
        categoryId: Long?, nameKeyword: String?, urlKeyword: String?, pageSize: Int, pageNum: Int
    ): IPage<UmsResource> {
        val page = Page<UmsResource>(pageNum, pageSize)
        return page(page) {
            val cond = mutableListOf<ColumnDeclaring<Boolean>>()
            if (categoryId != null) {
                cond += UmsResources.categoryId eq categoryId
            }
            if (!nameKeyword.isNullOrBlank()) {
                cond += UmsResources.name like nameKeyword
            }
            if (!urlKeyword.isNullOrBlank()) {
                cond += UmsResources.url like urlKeyword
            }
            cond.combineConditions()
        }
    }
}