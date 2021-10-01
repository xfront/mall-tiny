package com.macro.mall.tiny.common.config

import com.macro.mall.tiny.AllOpen
import com.macro.mall.tiny.common.domain.SwaggerProperties
import org.springframework.context.annotation.Bean
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.*
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket

/**
 * Swagger基础配置
 * Created by macro on 2020/7/16.
 */
@AllOpen
abstract class BaseSwaggerConfig {
    @Bean
    fun createRestApi(): Docket {
        val swaggerProperties = swaggerProperties()
        val docket = Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo(swaggerProperties))
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.apiBasePackage))
                .paths(PathSelectors.any())
                .build()
        if (swaggerProperties.enableSecurity) {
            docket.securitySchemes(securitySchemes())
                    .securityContexts(securityContexts())
        }
        return docket
    }

    private fun apiInfo(swaggerProperties: SwaggerProperties): ApiInfo {
        return ApiInfoBuilder().title(swaggerProperties.title)
                .description(swaggerProperties.description)
                .contact(Contact(swaggerProperties.contactName, swaggerProperties.contactUrl, swaggerProperties.contactEmail))
                .version(swaggerProperties.version)
                .build()
    }

    private fun securitySchemes(): List<SecurityScheme> { //设置请求头信息
        val apiKey = ApiKey("Authorization", "Authorization", "header")
        return mutableListOf(apiKey)
    }

    private fun securityContexts(): List<SecurityContext> {
        //设置需要登录认证的路径
        return mutableListOf(getContextByPath("/*/.*"))
    }

    private fun getContextByPath(pathRegex: String): SecurityContext {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex(pathRegex))
                .build()
    }

    private fun defaultAuth(): List<SecurityReference> {
        val authorizationScope = AuthorizationScope("global", "accessEverything")
        val authorizationScopes = arrayOf(authorizationScope)
        return mutableListOf(SecurityReference("Authorization", authorizationScopes))
    }

    /**
     * 自定义Swagger配置
     */
    abstract fun swaggerProperties(): SwaggerProperties
}