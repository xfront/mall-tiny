package com.macro.mall.tiny.modules.ums.mapper

import com.macro.mall.tiny.modules.ums.model.*
import org.ktorm.database.Database
import org.ktorm.entity.sequenceOf
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MapperConfig {
    @Autowired
    lateinit var database: Database

    @Bean
    fun adminLoginLogSeq() = database.sequenceOf(UmsAdminLoginLogs)

    @Bean
    fun adminSeq() = database.sequenceOf(UmsAdmins)

    @Bean
    fun adminRoleRelationSeq() = database.sequenceOf(UmsAdminRoleRelations)

    @Bean
    fun menuSeq() = database.sequenceOf(UmsMenus)

    @Bean
    fun resourceCategorySeq() = database.sequenceOf(UmsResourceCategorys)

    @Bean
    fun resourceSeq() = database.sequenceOf(UmsResources)

    @Bean
    fun roleSeq() = database.sequenceOf(UmsRoles)

    @Bean
    fun roleMenuRelationSeq() = database.sequenceOf(UmsRoleMenuRelations)

    @Bean
    fun roleResourceRelationSeq() = database.sequenceOf(UmsRoleResourceRelations)
}