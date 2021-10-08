package com.macro.mall.tiny.modules.ums.service

import com.github.xfront.ktormplus.IPage
import com.github.xfront.ktormplus.IService
import com.macro.mall.tiny.modules.ums.dto.UmsAdminParam
import com.macro.mall.tiny.modules.ums.dto.UpdateAdminPasswordParam
import com.macro.mall.tiny.modules.ums.model.UmsAdmin
import com.macro.mall.tiny.modules.ums.model.UmsAdmins
import com.macro.mall.tiny.modules.ums.model.UmsResource
import com.macro.mall.tiny.modules.ums.model.UmsRole
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.transaction.annotation.Transactional

/**
 * 后台管理员管理Service
 * Created by macro on 2018/4/26.
 */
interface UmsAdminService : IService<UmsAdmin, UmsAdmins> {
    /**
     * 根据用户名获取后台管理员
     */
    fun getAdminByUsername(username: String): UmsAdmin?

    /**
     * 注册功能
     */
    fun register(umsAdminParam: UmsAdminParam): UmsAdmin?

    /**
     * 登录功能
     * @param username 用户名
     * @param password 密码
     * @return 生成的JWT的token
     */
    fun login(username: String, password: String): String?

    /**
     * 刷新token的功能
     * @param oldToken 旧的token
     */
    fun refreshToken(oldToken: String): String?

    /**
     * 根据用户名或昵称分页查询用户
     */
    fun list(keyword: String?, pageSize: Int, pageNum: Int): IPage<UmsAdmin>

    /**
     * 修改指定用户信息
     */
    fun update(id: Long, admin: UmsAdmin): Boolean

    /**
     * 删除指定用户
     */
    fun delete(id: Long): Boolean

    /**
     * 修改用户角色关系
     */
    @Transactional
    fun updateRole(adminId: Long, roleIds: List<Long>): Int

    /**
     * 获取用户对于角色
     */
    fun getRoleList(adminId: Long): List<UmsRole>

    /**
     * 获取指定用户的可访问资源
     */
    fun getResourceList(adminId: Long): List<UmsResource>

    /**
     * 修改密码
     */
    fun updatePassword(updatePasswordParam: UpdateAdminPasswordParam): Int

    /**
     * 获取用户信息
     */
    fun loadUserByUsername(username: String): UserDetails
}