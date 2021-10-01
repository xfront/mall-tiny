package com.macro.mall.tiny.modules.ums.controller

import com.macro.mall.tiny.common.api.CommonPage
import com.macro.mall.tiny.common.api.CommonResult
import com.macro.mall.tiny.modules.ums.dto.UmsAdminLoginParam
import com.macro.mall.tiny.modules.ums.dto.UmsAdminParam
import com.macro.mall.tiny.modules.ums.dto.UpdateAdminPasswordParam
import com.macro.mall.tiny.modules.ums.model.UmsAdmin
import com.macro.mall.tiny.modules.ums.model.UmsRole
import com.macro.mall.tiny.modules.ums.service.UmsAdminService
import com.macro.mall.tiny.modules.ums.service.UmsRoleService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.security.Principal
import javax.servlet.http.HttpServletRequest

typealias StringMap = Map<String, String>

/**
 * 后台用户管理
 * Created by macro on 2018/4/26.
 */
@Controller
@Api(tags = ["UmsAdminController"], description = "后台用户管理")
@RequestMapping("/admin")
class UmsAdminController {
    @Value("\${jwt.tokenHeader}")
    lateinit var tokenHeader: String

    @Value("\${jwt.tokenHead}")
    lateinit var tokenHead: String

    @Autowired
    lateinit var adminService: UmsAdminService

    @Autowired
    lateinit var roleService: UmsRoleService

    @ApiOperation(value = "用户注册")
    @RequestMapping(value = ["/register"], method = [RequestMethod.POST])
    @ResponseBody
    fun register(@Validated @RequestBody umsAdminParam: UmsAdminParam): CommonResult<UmsAdmin> {
        val umsAdmin = adminService.register(umsAdminParam) ?: return CommonResult.failed()
        return CommonResult.success(umsAdmin)
    }

    @ApiOperation(value = "登录以后返回token")
    @RequestMapping(value = ["/login"], method = [RequestMethod.POST])
    @ResponseBody
    fun login(@Validated @RequestBody umsAdminLoginParam: UmsAdminLoginParam): CommonResult<Map<String, String>> {
        val token =
            adminService.login(umsAdminLoginParam.username, umsAdminLoginParam.password)
                ?: return CommonResult.validateFailed("用户名或密码错误")
        val tokenMap = mutableMapOf<String, String>()
        tokenMap["token"] = token
        tokenMap["tokenHead"] = tokenHead
        return CommonResult.success(tokenMap)
    }

    @ApiOperation(value = "刷新token")
    @RequestMapping(value = ["/refreshToken"], method = [RequestMethod.GET])
    @ResponseBody
    fun refreshToken(request: HttpServletRequest): CommonResult<StringMap> {
        val token = request.getHeader(tokenHeader)
        val refreshToken = adminService.refreshToken(token)
            ?: return CommonResult.failed("token已经过期！")
        val tokenMap = mutableMapOf<String, String>()
        tokenMap["token"] = refreshToken
        tokenMap["tokenHead"] = tokenHead
        return CommonResult.success(tokenMap)
    }

    @ApiOperation(value = "获取当前登录用户信息")
    @RequestMapping(value = ["/info"], method = [RequestMethod.GET])
    @ResponseBody
    fun getAdminInfo(principal: Principal?): CommonResult<Map<String, Any?>> {
        if (principal == null) {
            return CommonResult.unauthorized(null)
        }
        val username = principal.name
        val umsAdmin = adminService.getAdminByUsername(username) ?: return CommonResult.unauthorized(null)
        val data = mutableMapOf<String, Any?>()
        data["username"] = umsAdmin.username
        data["menus"] = roleService.getMenuList(umsAdmin.id)
        data["icon"] = umsAdmin.icon
        val roleList = adminService.getRoleList(umsAdmin.id)
        data["roles"] = roleList.map { it.name }

        return CommonResult.success(data)
    }

    @ApiOperation(value = "登出功能")
    @RequestMapping(value = ["/logout"], method = [RequestMethod.POST])
    @ResponseBody
    fun logout(): CommonResult<Any> {
        return CommonResult.success(null)
    }

    @ApiOperation("根据用户名或姓名分页获取用户列表")
    @RequestMapping(value = ["/list"], method = [RequestMethod.GET])
    @ResponseBody
    fun list(
        @RequestParam(value = "keyword", required = false) keyword: String?,
        @RequestParam(value = "pageSize", defaultValue = "5") pageSize: Long,
        @RequestParam(value = "pageNum", defaultValue = "1") pageNum: Long
    ): CommonResult<CommonPage<UmsAdmin>> {
        val adminList = adminService.list(keyword, pageSize, pageNum)
        return CommonResult.success(CommonPage.restPage(adminList))
    }

    @ApiOperation("获取指定用户信息")
    @RequestMapping(value = ["/{id}"], method = [RequestMethod.GET])
    @ResponseBody
    fun getItem(@PathVariable id: Long): CommonResult<UmsAdmin?> {
        val admin = adminService.getById(id)
        return CommonResult.success(admin)
    }

    @ApiOperation("修改指定用户信息")
    @RequestMapping(value = ["/update/{id}"], method = [RequestMethod.POST])
    @ResponseBody
    fun update(@PathVariable id: Long, @RequestBody admin: UmsAdmin): CommonResult<Any?> {
        val success = adminService.update(id, admin)
        return if (success) {
            CommonResult.success(null)
        } else CommonResult.failed()
    }

    @ApiOperation("修改指定用户密码")
    @RequestMapping(value = ["/updatePassword"], method = [RequestMethod.POST])
    @ResponseBody
    fun updatePassword(@Validated @RequestBody updatePasswordParam: UpdateAdminPasswordParam): CommonResult<Any> {
        val status = adminService.updatePassword(updatePasswordParam)
        return if (status > 0) {
            CommonResult.success(status)
        } else if (status == -1) {
            CommonResult.failed("提交参数不合法")
        } else if (status == -2) {
            CommonResult.failed("找不到该用户")
        } else if (status == -3) {
            CommonResult.failed("旧密码错误")
        } else {
            CommonResult.failed()
        }
    }

    @ApiOperation("删除指定用户信息")
    @RequestMapping(value = ["/delete/{id}"], method = [RequestMethod.POST])
    @ResponseBody
    fun delete(@PathVariable id: Long): CommonResult<Any> {
        val success = adminService.delete(id)
        return if (success) {
            CommonResult.success(null)
        } else CommonResult.failed()
    }

    @ApiOperation("修改帐号状态")
    @RequestMapping(value = ["/updateStatus/{id}"], method = [RequestMethod.POST])
    @ResponseBody
    fun updateStatus(@PathVariable id: Long, @RequestParam(value = "status") status: Int?): CommonResult<Any> {
        val umsAdmin = UmsAdmin()
        umsAdmin.status = status
        val success = adminService.update(id, umsAdmin)
        return if (success) {
            CommonResult.success(null)
        } else CommonResult.failed()
    }

    @ApiOperation("给用户分配角色")
    @RequestMapping(value = ["/role/update"], method = [RequestMethod.POST])
    @ResponseBody
    fun updateRole(
        @RequestParam("adminId") adminId: Long, @RequestParam("roleIds") roleIds: List<Long>
    ): CommonResult<Int> {
        val count = adminService.updateRole(adminId, roleIds)
        return if (count >= 0) {
            CommonResult.success(count)
        } else CommonResult.failed()
    }

    @ApiOperation("获取指定用户的角色")
    @RequestMapping(value = ["/role/{adminId}"], method = [RequestMethod.GET])
    @ResponseBody
    fun getRoleList(@PathVariable adminId: Long): CommonResult<List<UmsRole>> {
        val roleList = adminService.getRoleList(adminId)
        return CommonResult.success(roleList)
    }
}