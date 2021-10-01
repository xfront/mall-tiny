package com.macro.mall.tiny.security.component

import cn.hutool.core.util.URLUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.ConfigAttribute
import org.springframework.security.web.FilterInvocation
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource
import org.springframework.util.AntPathMatcher
import javax.annotation.PostConstruct

/**
 * 动态权限数据源，用于获取动态权限规则
 * Created by macro on 2020/2/7.
 */
class DynamicSecurityMetadataSource : FilterInvocationSecurityMetadataSource {
    @Autowired
    lateinit var dynamicSecurityService: DynamicSecurityService

    @PostConstruct
    fun loadDataSource() {
        configAttributeMap = dynamicSecurityService.loadDataSource()
    }

    fun clearDataSource() {
        configAttributeMap?.clear()
        configAttributeMap = null
    }

    @Throws(IllegalArgumentException::class)
    override fun getAttributes(o: Any): Collection<ConfigAttribute> {
        if (configAttributeMap == null) loadDataSource()
        val configAttributes = mutableListOf<ConfigAttribute>()

        //获取当前访问的路径
        val url = (o as FilterInvocation).requestUrl
        val path = URLUtil.getPath(url)
        val pathMatcher = AntPathMatcher()

        configAttributeMap!!.forEach { (pattern, value) ->
            if (pathMatcher.match(pattern, path)) {
                configAttributes.add(value)
            }
        }

        // 未设置操作请求权限，返回空集合
        return configAttributes
    }

    override fun getAllConfigAttributes(): Collection<ConfigAttribute> {
        return configAttributeMap?.map { it.value } ?: emptyList()
    }

    override fun supports(aClass: Class<*>?): Boolean {
        return true
    }

    companion object {
        private var configAttributeMap: MutableMap<String, ConfigAttribute>? = null
    }
}