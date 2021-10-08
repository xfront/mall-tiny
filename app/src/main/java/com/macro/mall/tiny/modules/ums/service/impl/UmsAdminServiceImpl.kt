package com.macro.mall.tiny.modules.ums.service.impl

import cn.hutool.core.util.StrUtil
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.macro.mall.security.util.JwtTokenUtil
import com.macro.mall.tiny.common.exception.Asserts
import com.macro.mall.tiny.domain.AdminUserDetails
import com.macro.mall.tiny.modules.ums.dto.UmsAdminParam
import com.macro.mall.tiny.modules.ums.dto.UpdateAdminPasswordParam
import com.macro.mall.tiny.modules.ums.mapper.UmsAdminLoginLogMapper
import com.macro.mall.tiny.modules.ums.mapper.UmsAdminMapper
import com.macro.mall.tiny.modules.ums.mapper.UmsResourceMapper
import com.macro.mall.tiny.modules.ums.mapper.UmsRoleMapper
import com.macro.mall.tiny.modules.ums.model.*
import com.macro.mall.tiny.modules.ums.service.UmsAdminCacheService
import com.macro.mall.tiny.modules.ums.service.UmsAdminRoleRelationService
import com.macro.mall.tiny.modules.ums.service.UmsAdminService
import org.slf4j.LoggerFactory
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.*

/**
 * 后台管理员管理Service实现类
 * Created by macro on 2018/4/26.
 */
@Service
class UmsAdminServiceImpl : ServiceImpl<UmsAdminMapper, UmsAdmin>(), UmsAdminService {
    @Autowired
    lateinit var jwtTokenUtil: JwtTokenUtil

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var loginLogMapper: UmsAdminLoginLogMapper

    @Autowired
    lateinit var adminCacheService: UmsAdminCacheService

    @Autowired
    lateinit var adminRoleRelationService: UmsAdminRoleRelationService

    @Autowired
    lateinit var roleMapper: UmsRoleMapper

    @Autowired
    lateinit var resourceMapper: UmsResourceMapper

    override fun getAdminByUsername(username: String): UmsAdmin? {
        var admin = adminCacheService.getAdmin(username)
        if (admin != null) return admin
        val wrapper = QueryWrapper<UmsAdmin>()
        wrapper.eq("username", username)
        //wrapper.lambda().eq(UmsAdmin::username, username)
        val adminList = list(wrapper)
        if (adminList != null && adminList.size > 0) {
            admin = adminList[0]
            adminCacheService.setAdmin(admin)
            return admin
        }
        return null
    }

    override fun register(umsAdminParam: UmsAdminParam): UmsAdmin? {
        val umsAdmin = UmsAdmin()
        BeanUtils.copyProperties(umsAdminParam, umsAdmin)
        umsAdmin.createTime = Date()
        umsAdmin.status = 1 //查询是否有相同用户名的用户
        val wrapper = QueryWrapper<UmsAdmin>()
        wrapper.eq("username", umsAdmin.username)
        //wrapper.lambda().eq(UmsAdmin::username, umsAdmin.username)
        val umsAdminList = list(wrapper)
        if (umsAdminList.size > 0) {
            return null
        }
        //将密码进行加密操作
        val encodePassword = passwordEncoder.encode(umsAdmin.password)
        umsAdmin.password = encodePassword
        baseMapper.insert(umsAdmin)
        return umsAdmin
    }

    override fun login(username: String, password: String): String {
        var token = ""
        //密码需要客户端加密后传递
        try {
            val userDetails = loadUserByUsername(username)
            if (!passwordEncoder.matches(password, userDetails.password)) {
                Asserts.fail("密码不正确")
            }
            if (!userDetails.isEnabled) {
                Asserts.fail("帐号已被禁用")
            }
            val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
            SecurityContextHolder.getContext().authentication = authentication
            token = jwtTokenUtil.generateToken(userDetails) //            updateLoginTimeByUsername(username);
            insertLoginLog(username)
        } catch (e: AuthenticationException) {
            LOGGER.warn("登录异常:{}", e.message)
        }
        return token
    }

    /**
     * 添加登录记录
     * @param username 用户名
     */
    private fun insertLoginLog(username: String) {
        val admin = getAdminByUsername(username) ?: return
        val loginLog = UmsAdminLoginLog()
        loginLog.adminId = admin.id
        loginLog.createTime = Date()
        val attributes = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes
        val request = attributes.request
        loginLog.ip = request.remoteAddr
        loginLogMapper.insert(loginLog)
    }

    /**
     * 根据用户名修改登录时间
     */
    private fun updateLoginTimeByUsername(username: String) {
        val record = UmsAdmin()
        record.loginTime = Date()
        val wrapper = QueryWrapper<UmsAdmin>()
        wrapper.eq("username", username)
        //wrapper.lambda().eq(UmsAdmin::username, username)
        update(record, wrapper)
    }

    override fun refreshToken(oldToken: String): String? {
        return jwtTokenUtil.refreshHeadToken(oldToken)
    }

    override fun list(keyword: String?, pageSize: Long, pageNum: Long): Page<UmsAdmin> {
        val page = Page<UmsAdmin>(pageNum, pageSize)
        val wrapper = QueryWrapper<UmsAdmin>()
        if (!keyword.isNullOrBlank()) {
            wrapper.like("username", keyword)
            wrapper.or { wrapper.like("nick_name", keyword) }
        }
        return page(page, wrapper)
    }

    override fun update(id: Long, admin: UmsAdmin): Boolean {
        admin.id = id
        val rawAdmin = getById(id) ?: return false
        if (rawAdmin.password == admin.password) { //与原加密密码相同的不需要修改
            admin.password = null
        } else { //与原加密密码不同的需要加密修改
            if (StrUtil.isEmpty(admin.password)) {
                admin.password = null
            } else {
                admin.password = passwordEncoder.encode(admin.password)
            }
        }
        val success = updateById(admin)
        adminCacheService.delAdmin(id)
        return success
    }

    override fun delete(id: Long): Boolean {
        adminCacheService.delAdmin(id)
        val success = removeById(id)
        adminCacheService.delResourceList(id)
        return success
    }

    override fun updateRole(adminId: Long, roleIds: List<Long>): Int {
        val count = roleIds.size
        //先删除原来的关系
        val wrapper = QueryWrapper<UmsAdminRoleRelation>()
        wrapper.eq("admin_id", adminId)
        //wrapper.lambda().eq(UmsAdminRoleRelation::adminId, adminId)
        adminRoleRelationService.remove(wrapper) //建立新关系
        if (roleIds.isNotEmpty()) {
            val list = mutableListOf<UmsAdminRoleRelation>()
            for (roleId in roleIds) {
                val roleRelation = UmsAdminRoleRelation(adminId, roleId)
                list.add(roleRelation)
            }
            adminRoleRelationService.saveBatch(list)
        }
        adminCacheService.delResourceList(adminId)
        return count
    }

    override fun getRoleList(adminId: Long): List<UmsRole> {
        return roleMapper.getRoleList(adminId)
    }

    override fun getResourceList(adminId: Long): List<UmsResource> {
        var resourceList = adminCacheService.getResourceList(adminId)
        if (!resourceList.isNullOrEmpty()) {
            return resourceList
        }
        resourceList = resourceMapper.getResourceList(adminId)
        if (resourceList.isNotEmpty()) {
            adminCacheService.setResourceList(adminId, resourceList)
        }
        return resourceList
    }

    override fun updatePassword(param: UpdateAdminPasswordParam): Int {
        if (param.username.isNullOrBlank()
                ||  param.oldPassword.isNullOrBlank()
                || param.newPassword.isNullOrBlank()) {
            return -1
        }
        val wrapper = QueryWrapper<UmsAdmin>()
        wrapper.eq("username", param.username)
        //wrapper.lambda().eq(UmsAdmin::username, param.username)
        val adminList = list(wrapper)
        if (adminList.isEmpty()) {
            return -2
        }
        val umsAdmin = adminList[0] ?: return -2
        if (!passwordEncoder.matches(param.oldPassword, umsAdmin.password)) {
            return -3
        }
        umsAdmin.password = (passwordEncoder.encode(param.newPassword))
        updateById(umsAdmin)
        adminCacheService.delAdmin(umsAdmin.id)
        return 1
    }

    override fun loadUserByUsername(username: String): UserDetails { //获取用户信息
        val admin = getAdminByUsername(username)
        if (admin != null) {
            val resourceList = getResourceList(admin.id)
            return AdminUserDetails(admin, resourceList)
        }
        throw UsernameNotFoundException("用户名或密码错误")
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(UmsAdminServiceImpl::class.java)
    }
}