package com.macro.mall.tiny


import org.apache.commons.logging.LogFactory
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import javax.annotation.Resource
import java.io.Serializable
data class User(val username: String="", val age: Int?=-1) : Serializable

@SpringBootTest
class MallTinyApplicationTests {
    @Test
    fun contextLoads() {
    }

    val log = LogFactory.getLog(MallTinyApplicationTests::class.java)!!
    @Resource
    lateinit var stringRedisTemplate: StringRedisTemplate
    @Resource
    lateinit var redisTemplate: RedisTemplate<String, User>
    @Test
    fun `redis string test"`() {
        // 保存字符串
        stringRedisTemplate.opsForValue().set("url", "http://quanke.name")
        log.info("全科的博客地址: ${stringRedisTemplate.opsForValue().get("url")}")
    }
    @Test
    fun `redis object test"`() {
        // 保存对象
        val user = User("超人", 20)
        redisTemplate.opsForValue().set(user.username, user)
        log.info("超人的年龄：${redisTemplate.opsForValue().get("超人")?.age}")
    }
}