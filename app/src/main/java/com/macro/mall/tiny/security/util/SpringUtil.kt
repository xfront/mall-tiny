package com.macro.mall.security.util

import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

/**
 * Spring工具类
 * Created by macro on 2020/3/3.
 */
@Component
class SpringUtil : ApplicationContextAware {
    @Throws(BeansException::class)
    override fun setApplicationContext(ctx: ApplicationContext) {
        context = ctx
    }

    companion object {
        // 获取applicationContext
        lateinit var context: ApplicationContext

        // 通过name获取Bean
        fun getBean(name: String): Any? {
            return context.getBean(name)
        }

        // 通过class获取Bean
        fun <T> getBean(clazz: Class<T>): T {
            return context.getBean(clazz)
        }

        // 通过name,以及Clazz返回指定的Bean
        fun <T> getBean(name: String, clazz: Class<T>): T {
            return context.getBean(name, clazz)
        }
    }
}