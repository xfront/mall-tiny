package com.macro.mall.tiny.modules.ums.controller

import com.macro.mall.tiny.common.api.CommonPage
import com.macro.mall.tiny.common.api.CommonResult
import com.macro.mall.tiny.modules.ums.model.UmsMenu
import com.macro.mall.tiny.modules.ums.service.UmsMenuService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

/**
 * 后台菜单管理Controller
 * Created by macro on 2020/2/4.
 */
@Controller
@Api(tags = ["后台菜单管理"])
@RequestMapping("/menu")
class UmsMenuController {
    @Autowired
    lateinit var menuService: UmsMenuService

    @ApiOperation("添加后台菜单")
    @RequestMapping(value = ["/create"], method = [RequestMethod.POST])
    @ResponseBody
    fun create(@RequestBody umsMenu: UmsMenu): CommonResult<Any> {
        val success = menuService.create(umsMenu)
        return if (success) {
            CommonResult.success(null)
        } else {
            CommonResult.failed()
        }
    }

    @ApiOperation("修改后台菜单")
    @RequestMapping(value = ["/update/{id}"], method = [RequestMethod.POST])
    @ResponseBody
    fun update(
        @PathVariable id: Long, @RequestBody umsMenu: UmsMenu
    ): CommonResult<Any> {
        val success = menuService.update(id, umsMenu)
        return if (success) {
            CommonResult.success(null)
        } else {
            CommonResult.failed()
        }
    }

    @ApiOperation("根据ID获取菜单详情")
    @RequestMapping(value = ["/{id}"], method = [RequestMethod.GET])
    @ResponseBody
    fun getItem(@PathVariable id: Long): CommonResult<UmsMenu?> {
        val umsMenu = menuService.getById(id)
        return CommonResult.success(umsMenu)
    }

    @ApiOperation("根据ID删除后台菜单")
    @RequestMapping(value = ["/delete/{id}"], method = [RequestMethod.POST])
    @ResponseBody
    fun delete(@PathVariable id: Long): CommonResult<Any> {
        val success = menuService.removeById(id)
        return if (success) {
            CommonResult.success(null)
        } else {
            CommonResult.failed()
        }
    }

    @ApiOperation("分页查询后台菜单")
    @RequestMapping(value = ["/list/{parentId}"], method = [RequestMethod.GET])
    @ResponseBody
    fun list(
        @PathVariable parentId: Long,
        @RequestParam(value = "pageSize", defaultValue = "5") pageSize: Int,
        @RequestParam(value = "pageNum", defaultValue = "1") pageNum: Int
    ): CommonResult<CommonPage<UmsMenu>> {
        val menuList = menuService.list(parentId, pageSize, pageNum)
        return CommonResult.success(CommonPage.restPage(menuList))
    }

    @ApiOperation("树形结构返回所有菜单列表")
    @RequestMapping(value = ["/treeList"], method = [RequestMethod.GET])
    @ResponseBody
    fun treeList(): CommonResult<List<UmsMenu>> {
        val list = menuService.treeList()
        return CommonResult.success(list)
    }

    @ApiOperation("修改菜单显示状态")
    @RequestMapping(value = ["/updateHidden/{id}"], method = [RequestMethod.POST])
    @ResponseBody
    fun updateHidden(@PathVariable id: Long, @RequestParam("hidden") hidden: Int): CommonResult<Any> {
        val success = menuService.updateHidden(id, hidden)
        return if (success) {
            CommonResult.success(null)
        } else {
            CommonResult.failed()
        }
    }
}