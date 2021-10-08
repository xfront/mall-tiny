package com.macro.mall.tiny.config

import com.macro.mall.tiny.modules.ums.service.UmsAdminService
import com.macro.mall.tiny.modules.ums.service.UmsResourceService
import com.macro.mall.tiny.security.component.DynamicSecurityService
import com.macro.mall.tiny.security.config.SecurityConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.ConfigAttribute
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import java.util.concurrent.ConcurrentHashMap

/**
 * mall-security模块相关配置
 * Created by macro on 2019/11/9.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class MallSecurityConfig : SecurityConfig() {
    @Autowired
    lateinit var adminService: UmsAdminService

    @Autowired
    lateinit var resourceService: UmsResourceService

    @Bean
    public override fun userDetailsService(): UserDetailsService {
        //获取登录用户信息
        return UserDetailsService { adminService.loadUserByUsername(it) }
    }

    @Bean
    fun dynamicSecurityService(): DynamicSecurityService {
        return object : DynamicSecurityService {
            override fun loadDataSource(): MutableMap<String, ConfigAttribute> {
                val map = ConcurrentHashMap<String, ConfigAttribute>()
                val resourceList = resourceService.list()
                for (resource in resourceList) {
                    map[resource.url!!] =
                        org.springframework.security.access.SecurityConfig("""${resource.id}:${resource.name}""")
                }
                return map
            }
        }
    }
}