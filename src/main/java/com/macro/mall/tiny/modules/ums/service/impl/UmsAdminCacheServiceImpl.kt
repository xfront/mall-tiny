package com.macro.mall.tiny.modules.ums.service.impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.macro.mall.tiny.common.service.RedisService
import com.macro.mall.tiny.modules.ums.mapper.UmsAdminMapper
import com.macro.mall.tiny.modules.ums.model.UmsAdmin
import com.macro.mall.tiny.modules.ums.model.UmsAdminRoleRelation
import com.macro.mall.tiny.modules.ums.model.UmsResource
import com.macro.mall.tiny.modules.ums.service.UmsAdminCacheService
import com.macro.mall.tiny.modules.ums.service.UmsAdminRoleRelationService
import com.macro.mall.tiny.modules.ums.service.UmsAdminService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

/**
 * 后台用户缓存管理Service实现类
 * Created by macro on 2020/3/13.
 */
@Service
class UmsAdminCacheServiceImpl : UmsAdminCacheService {
    @Autowired
    lateinit var adminService: UmsAdminService

    @Autowired
    lateinit var redisService: RedisService

    @Autowired
    lateinit var adminMapper: UmsAdminMapper

    @Autowired
    lateinit var adminRoleRelationService: UmsAdminRoleRelationService

    @Value("\${redis.database}")
    lateinit var REDIS_DATABASE: String

    @Value("\${redis.expire.common}")
    private var REDIS_EXPIRE: Long = 30

    @Value("\${redis.key.admin}")
    lateinit var REDIS_KEY_ADMIN: String

    @Value("\${redis.key.resourceList}")
    lateinit var REDIS_KEY_RESOURCE_LIST: String

    override fun delAdmin(adminId: Long) {
        val admin = adminService.getById(adminId)
        if (admin != null) {
            val key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + admin.username
            redisService.del(key)
        }
    }

    override fun delResourceList(adminId: Long) {
        val key = "$REDIS_DATABASE:$REDIS_KEY_RESOURCE_LIST:$adminId"
        redisService.del(key)
    }

    override fun delResourceListByRole(roleId: Long) {
        val wrapper = QueryWrapper<UmsAdminRoleRelation>()
        wrapper.eq("role_id", roleId)
        val relationList = adminRoleRelationService.list(wrapper)
        if (!relationList.isNullOrEmpty()) {
            val keyPrefix = "$REDIS_DATABASE:$REDIS_KEY_RESOURCE_LIST:"
            val keys = relationList.map { keyPrefix + it.adminId }
            redisService.del(keys)
        }
    }

    override fun delResourceListByRoleIds(roleIds: List<Long>) {
        val wrapper = QueryWrapper<UmsAdminRoleRelation>()
        wrapper.`in`("role_id", roleIds)
        val relationList = adminRoleRelationService.list(wrapper)

        val keyPrefix = "$REDIS_DATABASE:$REDIS_KEY_RESOURCE_LIST:"
        val keys = relationList.map { keyPrefix + it.adminId }
        redisService.del(keys)
    }

    override fun delResourceListByResource(resourceId: Long) {
        val adminIdList = adminMapper.getAdminIdList(resourceId)
        val keyPrefix = "$REDIS_DATABASE:$REDIS_KEY_RESOURCE_LIST:"
        val keys = adminIdList.map { keyPrefix + it }
        redisService.del(keys)
    }

    override fun getAdmin(username: String): UmsAdmin? {
        val key = "$REDIS_DATABASE:$REDIS_KEY_ADMIN:$username"
        return redisService.get(key) as? UmsAdmin
    }

    override fun setAdmin(admin: UmsAdmin) {
        val key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + admin.username
        redisService.set(key, admin, REDIS_EXPIRE)
    }

    override fun getResourceList(adminId: Long): List<UmsResource>? {
        val key = "$REDIS_DATABASE:$REDIS_KEY_RESOURCE_LIST:$adminId"
        return redisService[key] as? List<UmsResource>
    }

    override fun setResourceList(adminId: Long, resourceList: List<UmsResource>) {
        val key = "$REDIS_DATABASE:$REDIS_KEY_RESOURCE_LIST:$adminId"
        redisService[key, resourceList] = REDIS_EXPIRE
    }
}