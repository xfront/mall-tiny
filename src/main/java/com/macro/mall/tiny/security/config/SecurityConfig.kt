package com.macro.mall.tiny.security.config

import com.macro.mall.security.util.JwtTokenUtil
import com.macro.mall.tiny.AllOpen
import com.macro.mall.tiny.security.component.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

/**
 * 对SpringSecurity的配置的扩展，支持自定义白名单资源路径和查询用户逻辑
 * Created by macro on 2019/11/5.
 */
@AllOpen
open class SecurityConfig : WebSecurityConfigurerAdapter() {
    @Autowired(required = false)
    val dynamicSecurityService: DynamicSecurityService? = null

    @Throws(Exception::class)
    override fun configure(httpSecurity: HttpSecurity) {
        val registry = httpSecurity.authorizeRequests() //不需要保护的资源路径允许访问
        for (url in ignoreUrlsConfig().urls) {
            registry.antMatchers(url)
                    .permitAll()
        } //允许跨域请求的OPTIONS请求
        registry.antMatchers(HttpMethod.OPTIONS)
                .permitAll() // 任何请求需要身份认证
        registry.and()
                .authorizeRequests()
                .anyRequest()
                .authenticated() // 关闭跨站请求防护及不使用session
                .and()
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 自定义权限拒绝处理类
                .and()
                .exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler())
                .authenticationEntryPoint(restAuthenticationEntryPoint()) // 自定义权限拦截器JWT过滤器
                .and()
                .addFilterBefore(jwtAuthenticationTokenFilter(),
                        UsernamePasswordAuthenticationFilter::class.java) //有动态权限配置时添加动态权限校验过滤器
        if (dynamicSecurityService != null) {
            registry.and()
                    .addFilterBefore(dynamicSecurityFilter(), FilterSecurityInterceptor::class.java)
        }
    }

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder())
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun jwtAuthenticationTokenFilter(): JwtAuthenticationTokenFilter {
        return JwtAuthenticationTokenFilter()
    }

    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Bean
    fun restfulAccessDeniedHandler(): RestfulAccessDeniedHandler {
        return RestfulAccessDeniedHandler()
    }

    @Bean
    fun restAuthenticationEntryPoint(): RestAuthenticationEntryPoint {
        return RestAuthenticationEntryPoint()
    }

    @Bean
    fun ignoreUrlsConfig(): IgnoreUrlsConfig {
        return IgnoreUrlsConfig()
    }

    @Bean
    fun jwtTokenUtil(): JwtTokenUtil {
        return JwtTokenUtil()
    }

    @ConditionalOnBean(name = ["dynamicSecurityService"])
    @Bean
    fun dynamicAccessDecisionManager(): DynamicAccessDecisionManager {
        return DynamicAccessDecisionManager()
    }

    @ConditionalOnBean(name = ["dynamicSecurityService"])
    @Bean
    fun dynamicSecurityFilter(): DynamicSecurityFilter {
        return DynamicSecurityFilter()
    }

    @ConditionalOnBean(name = ["dynamicSecurityService"])
    @Bean
    fun dynamicSecurityMetadataSource(): DynamicSecurityMetadataSource {
        return DynamicSecurityMetadataSource()
    }
}