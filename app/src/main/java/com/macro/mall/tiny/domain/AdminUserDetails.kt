package com.macro.mall.tiny.domain

import com.macro.mall.tiny.modules.ums.model.UmsAdmin
import com.macro.mall.tiny.modules.ums.model.UmsResource
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * SpringSecurity需要的用户详情
 * Created by macro on 2018/4/26.
 */
class AdminUserDetails(private val umsAdmin: UmsAdmin, private val resourceList: List<UmsResource>) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> { //返回当前用户的角色
        return resourceList
                .map { role ->
                    SimpleGrantedAuthority("""${role.id}:${role.name}""")
                }
    }

    override fun getPassword(): String {
        return umsAdmin.password ?: "(null)"
    }

    override fun getUsername(): String {
        return umsAdmin.username ?: "(null)"
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return umsAdmin.status == 1
    }
}