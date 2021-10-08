package com.macro.mall.tiny.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import springfox.documentation.oas.annotations.EnableOpenApi
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.spi.DocumentationType
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.builders.PathSelectors

@EnableOpenApi
@Configuration
@Import(BeanValidatorPluginsConfiguration::class)
class Knife4jConfiguration {
    @Bean(value = ["defaultApi2"])
    fun defaultApi2(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .apiInfo(ApiInfoBuilder()
                        .title("mall-tiny")
                        .description("mall-tiny项目骨架")
                        .version("1.0")
                        .build()
                ) //分组名称
                .groupName("ums")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.macro.mall.tiny.modules.ums"))
                .paths(PathSelectors.any())
                .build()
    }
}