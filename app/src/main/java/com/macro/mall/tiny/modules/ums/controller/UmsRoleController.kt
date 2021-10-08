package com.macro.mall.tiny.modules.ums.controller

import com.macro.mall.tiny.common.api.CommonPage
import com.macro.mall.tiny.common.api.CommonResult
import com.macro.mall.tiny.modules.ums.model.UmsMenu
import com.macro.mall.tiny.modules.ums.model.UmsResource
import com.macro.mall.tiny.modules.ums.model.UmsRole
import com.macro.mall.tiny.modules.ums.service.UmsRoleService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

/**
 * 后台用户角色管理
 * Created by macro on 2018/9/30.
 */
@Controller
@Api(tags = ["UmsRoleController"], description = "后台用户角色管理")
@RequestMapping("/role")
class UmsRoleController {
    @Autowired
    lateinit var roleService: UmsRoleService

    @ApiOperation("添加角色")
    @RequestMapping(value = ["/create"], method = [RequestMethod.POST])
    @ResponseBody
    fun create(@RequestBody role: UmsRole): CommonResult<Any> {
        val success = roleService.create(role)
        return if (success) {
            CommonResult.success(null)
        } else CommonResult.failed()
    }

    @ApiOperation("修改角色")
    @RequestMapping(value = ["/update/{id}"], method = [RequestMethod.POST])
    @ResponseBody
    fun update(@PathVariable id: Long, @RequestBody role: UmsRole): CommonResult<Any> {
        role.id = id
        val success = roleService.updateById(role)
        return if (success) {
            CommonResult.success(null)
        } else CommonResult.failed()
    }

    @ApiOperation("批量删除角色")
    @RequestMapping(value = ["/delete"], method = [RequestMethod.POST])
    @ResponseBody
    fun delete(@RequestParam("ids") ids: List<Long>): CommonResult<Any> {
        val success = roleService.delete(ids)
        return if (success) {
            CommonResult.success(null)
        } else CommonResult.failed()
    }

    @ApiOperation("获取所有角色")
    @RequestMapping(value = ["/listAll"], method = [RequestMethod.GET])
    @ResponseBody
    fun listAll(): CommonResult<List<UmsRole>> {
        val roleList = roleService.list() as? List<UmsRole>
        return CommonResult.success(roleList)
    }

    @ApiOperation("根据角色名称分页获取角色列表")
    @RequestMapping(value = ["/list"], method = [RequestMethod.GET])
    @ResponseBody
    fun list(
        @RequestParam(value = "keyword", required = false) keyword: String?,
        @RequestParam(value = "pageSize", defaultValue = "5") pageSize: Long,
        @RequestParam(value = "pageNum", defaultValue = "1") pageNum: Long
    ): CommonResult<CommonPage<UmsRole>> {
        val roleList = roleService.list(keyword, pageSize, pageNum)
        return CommonResult.success(CommonPage.restPage(roleList))
    }

    @ApiOperation("修改角色状态")
    @RequestMapping(value = ["/updateStatus/{id}"], method = [RequestMethod.POST])
    @ResponseBody
    fun updateStatus(@PathVariable id: Long, @RequestParam(value = "status") status: Int?): CommonResult<Any> {
        val umsRole = UmsRole()
        umsRole.id = id
        umsRole.status = status
        val success = roleService.updateById(umsRole)
        return if (success) {
            CommonResult.success(null)
        } else CommonResult.failed()
    }

    @ApiOperation("获取角色相关菜单")
    @RequestMapping(value = ["/listMenu/{roleId}"], method = [RequestMethod.GET])
    @ResponseBody
    fun listMenu(@PathVariable roleId: Long): CommonResult<List<UmsMenu>> {
        val roleList = roleService.listMenu(roleId)
        return CommonResult.success(roleList)
    }

    @ApiOperation("获取角色相关资源")
    @RequestMapping(value = ["/listResource/{roleId}"], method = [RequestMethod.GET])
    @ResponseBody
    fun listResource(@PathVariable roleId: Long): CommonResult<List<UmsResource>> {
        val roleList = roleService.listResource(roleId)
        return CommonResult.success(roleList)
    }

    @ApiOperation("给角色分配菜单")
    @RequestMapping(value = ["/allocMenu"], method = [RequestMethod.POST])
    @ResponseBody
    fun allocMenu(@RequestParam roleId: Long, @RequestParam menuIds: List<Long>): CommonResult<Any> {
        val count = roleService.allocMenu(roleId, menuIds)
        return CommonResult.success(count)
    }

    @ApiOperation("给角色分配资源")
    @RequestMapping(value = ["/allocResource"], method = [RequestMethod.POST])
    @ResponseBody
    fun allocResource(@RequestParam roleId: Long, @RequestParam resourceIds: List<Long>): CommonResult<Any> {
        val count = roleService.allocResource(roleId, resourceIds)
        return CommonResult.success(count)
    }
}