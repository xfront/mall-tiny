package com.macro.mall.tiny.common.domain

/**
 * Swagger自定义配置
 * Created by macro on 2020/7/16.
 */
class SwaggerProperties {
    /**
     * API文档生成基础路径
     */
    lateinit var apiBasePackage: String

    /**
     * 是否要启用登录认证
     */
    var enableSecurity = false

    /**
     * 文档标题
     */
    var title: String? = null

    /**
     * 文档描述
     */
    var description: String? = null

    /**
     * 文档版本
     */
    var version: String? = null

    /**
     * 文档联系人姓名
     */
    var contactName: String? = null

    /**
     * 文档联系人网址
     */
    var contactUrl: String? = null

    /**
     * 文档联系人邮箱
     */
    var contactEmail: String? = null
}