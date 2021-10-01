package com.macro.mall.tiny.modules.ums.service

import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.IService
import com.macro.mall.tiny.modules.ums.model.UmsResource

/**
 * 后台资源管理Service
 * Created by macro on 2020/2/2.
 */
interface UmsResourceService : IService<UmsResource> {
    /**
     * 添加资源
     */
    fun create(umsResource: UmsResource): Boolean

    /**
     * 修改资源
     */
    fun update(id: Long, umsResource: UmsResource): Boolean

    /**
     * 删除资源
     */
    fun delete(id: Long): Boolean

    /**
     * 分页查询资源
     */
    fun list(categoryId: Long?, nameKeyword: String?, urlKeyword: String?, pageSize: Long, pageNum: Long): Page<UmsResource>
}