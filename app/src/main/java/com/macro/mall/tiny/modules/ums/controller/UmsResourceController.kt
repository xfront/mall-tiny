package com.macro.mall.tiny.modules.ums.controller

import com.macro.mall.tiny.common.api.CommonPage
import com.macro.mall.tiny.common.api.CommonResult
import com.macro.mall.tiny.modules.ums.model.UmsResource
import com.macro.mall.tiny.modules.ums.service.UmsResourceService
import com.macro.mall.tiny.security.component.DynamicSecurityMetadataSource
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

/**
 * 后台资源管理Controller
 * Created by macro on 2020/2/4.
 */
@Controller
@Api(tags = ["UmsResourceController"], description = "后台资源管理")
@RequestMapping("/resource")
class UmsResourceController {
    @Autowired
    lateinit var resourceService: UmsResourceService

    @Autowired
    lateinit var dynamicSecurityMetadataSource: DynamicSecurityMetadataSource

    @ApiOperation("添加后台资源")
    @RequestMapping(value = ["/create"], method = [RequestMethod.POST])
    @ResponseBody
    fun create(@RequestBody umsResource: UmsResource): CommonResult<Any> {
        val success = resourceService.create(umsResource)
        dynamicSecurityMetadataSource.clearDataSource()
        return if (success) {
            CommonResult.success(null)
        } else {
            CommonResult.failed()
        }
    }

    @ApiOperation("修改后台资源")
    @RequestMapping(value = ["/update/{id}"], method = [RequestMethod.POST])
    @ResponseBody
    fun update(
        @PathVariable id: Long, @RequestBody umsResource: UmsResource
    ): CommonResult<Any> {
        val success = resourceService.update(id, umsResource)
        dynamicSecurityMetadataSource.clearDataSource()
        return if (success) {
            CommonResult.success(null)
        } else {
            CommonResult.failed()
        }
    }

    @ApiOperation("根据ID获取资源详情")
    @RequestMapping(value = ["/{id}"], method = [RequestMethod.GET])
    @ResponseBody
    fun getItem(@PathVariable id: Long?): CommonResult<UmsResource?> {
        val umsResource = resourceService.getById(id)
        return CommonResult.success<UmsResource?>(umsResource)
    }

    @ApiOperation("根据ID删除后台资源")
    @RequestMapping(value = ["/delete/{id}"], method = [RequestMethod.POST])
    @ResponseBody
    fun delete(@PathVariable id: Long): CommonResult<Any> {
        val success = resourceService.delete(id)
        dynamicSecurityMetadataSource.clearDataSource()
        return if (success) {
            CommonResult.success(null)
        } else {
            CommonResult.failed()
        }
    }

    @ApiOperation("分页模糊查询后台资源")
    @RequestMapping(value = ["/list"], method = [RequestMethod.GET])
    @ResponseBody
    fun list(
        @RequestParam(required = false) categoryId: Long?,
        @RequestParam(required = false) nameKeyword: String?,
        @RequestParam(required = false) urlKeyword: String?,
        @RequestParam(value = "pageSize", defaultValue = "5") pageSize: Long,
        @RequestParam(value = "pageNum", defaultValue = "1") pageNum: Long
    ): CommonResult<CommonPage<UmsResource>> {
        val resourceList = resourceService.list(categoryId, nameKeyword, urlKeyword, pageSize, pageNum)
        return CommonResult.success(CommonPage.restPage(resourceList))
    }

    @ApiOperation("查询所有后台资源")
    @RequestMapping(value = ["/listAll"], method = [RequestMethod.GET])
    @ResponseBody
    fun listAll(): CommonResult<List<UmsResource>> {
        val resourceList = resourceService.list()
        return CommonResult.success(resourceList)
    }
}