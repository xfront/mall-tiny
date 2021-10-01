package com.macro.mall.tiny.config

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize
import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement

/**
 * MyBatis配置类
 * Created by macro on 2019/4/8.
 */
@Configuration
@EnableTransactionManagement
@MapperScan("com.macro.mall.tiny.modules.*.mapper")
class MyBatisConfig {
    @Bean
    fun paginationInterceptor(): PaginationInterceptor {
        val paginationInterceptor = PaginationInterceptor()
        paginationInterceptor.setCountSqlParser(JsqlParserCountOptimize(true))
        return paginationInterceptor
    }
}