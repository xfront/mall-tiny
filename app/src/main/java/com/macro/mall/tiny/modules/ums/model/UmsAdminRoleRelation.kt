package com.macro.mall.tiny.modules.ums.model

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.long


//实体类： 后台用户和角色关系表
interface UmsAdminRoleRelation : Entity<UmsAdminRoleRelation> {
    companion object : Entity.Factory<UmsAdminRoleRelation>()


    var id: Long

    var adminId: Long?

    var roleId: Long?
}


//表定义： 后台用户和角色关系表
open class UmsAdminRoleRelations(alias: String?) : Table<UmsAdminRoleRelation>("ums_admin_role_relation", alias) {
    companion object : UmsAdminRoleRelations(null)

    override fun aliased(alias: String) = UmsAdminRoleRelations(alias)


    val id = long("id").primaryKey()
            .bindTo { it.id }

    val adminId = long("admin_id").bindTo { it.adminId }

    val roleId = long("role_id").bindTo { it.roleId }
}
