package com.macro.mall.tiny.common.config

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import com.macro.mall.tiny.AllOpen
import com.macro.mall.tiny.common.service.RedisService
import com.macro.mall.tiny.common.service.impl.RedisServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.cache.RedisCacheWriter
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration


/**
 * Redis基础配置
 * Created by macro on 2020/6/19.
 */
@AllOpen
open class BaseRedisConfig {
    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> {
        val serializer = redisSerializer()
        val redisTemplate = RedisTemplate<String, Any>()
        redisTemplate.setConnectionFactory(redisConnectionFactory)
        redisTemplate.keySerializer = StringRedisSerializer()
        redisTemplate.valueSerializer = serializer
        redisTemplate.hashKeySerializer = StringRedisSerializer()
        redisTemplate.hashValueSerializer = serializer
        redisTemplate.afterPropertiesSet()
        return redisTemplate
    }

    @Bean
    fun redisSerializer(): RedisSerializer<Any> {
        //创建JSON序列化器
        val serializer = Jackson2JsonRedisSerializer(Any::class.java)
        val mapper = ObjectMapper()
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.EVERYTHING)
        serializer.setObjectMapper(mapper)
        return serializer
    }

    @Bean
    fun redisCacheManager(redisConnectionFactory: RedisConnectionFactory): RedisCacheManager {
        val redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory) //设置Redis缓存有效期为1天
        val redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer()))
                .entryTtl(Duration.ofDays(1))
        return RedisCacheManager(redisCacheWriter, redisCacheConfiguration)
    }

    @Bean
    fun redisService(): RedisService {
        return RedisServiceImpl()
    }
}