package com.macro.mall.tiny.modules.ums.controller

import com.macro.mall.tiny.common.api.CommonResult
import com.macro.mall.tiny.modules.ums.model.UmsResourceCategory
import com.macro.mall.tiny.modules.ums.service.UmsResourceCategoryService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

/**
 * 后台资源分类管理Controller
 * Created by macro on 2020/2/5.
 */
@Controller
@Api(tags = ["后台资源分类管理"])
@RequestMapping("/resourceCategory")
class UmsResourceCategoryController {
    @Autowired
    lateinit var resourceCategoryService: UmsResourceCategoryService

    @ApiOperation("查询所有后台资源分类")
    @RequestMapping(value = ["/listAll"], method = [RequestMethod.GET])
    @ResponseBody
    fun listAll(): CommonResult<List<UmsResourceCategory>?> {
        val resourceList = resourceCategoryService.listAll()
        return CommonResult.success(resourceList)
    }

    @ApiOperation("添加后台资源分类")
    @RequestMapping(value = ["/create"], method = [RequestMethod.POST])
    @ResponseBody
    fun create(@RequestBody umsResourceCategory: UmsResourceCategory): CommonResult<Any> {
        val success = resourceCategoryService.create(umsResourceCategory)
        return if (success) {
            CommonResult.success(null)
        } else {
            CommonResult.failed()
        }
    }

    @ApiOperation("修改后台资源分类")
    @RequestMapping(value = ["/update/{id}"], method = [RequestMethod.POST])
    @ResponseBody
    fun update(
        @PathVariable id: Long, @RequestBody umsResourceCategory: UmsResourceCategory
    ): CommonResult<Any> {
        umsResourceCategory.id = id
        val success = resourceCategoryService.updateById(umsResourceCategory)
        return if (success) {
            CommonResult.success(null)
        } else {
            CommonResult.failed()
        }
    }

    @ApiOperation("根据ID删除后台资源")
    @RequestMapping(value = ["/delete/{id}"], method = [RequestMethod.POST])
    @ResponseBody
    fun delete(@PathVariable id: Long): CommonResult<Any> {
        val success = resourceCategoryService.removeById(id)
        return if (success) {
            CommonResult.success(null)
        } else {
            CommonResult.failed()
        }
    }
}