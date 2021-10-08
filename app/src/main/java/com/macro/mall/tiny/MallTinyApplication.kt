package com.macro.mall.tiny

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

annotation class AllOpen

@SpringBootApplication
class  MallTinyApplication

fun main(args: Array<String>) {
    SpringApplication.run(MallTinyApplication::class.java, *args)
}