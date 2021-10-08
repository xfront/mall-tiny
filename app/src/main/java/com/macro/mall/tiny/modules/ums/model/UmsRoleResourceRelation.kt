package com.macro.mall.tiny.modules.ums.model

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.long


//实体类： 后台角色资源关系表
interface UmsRoleResourceRelation : Entity<UmsRoleResourceRelation> {
    companion object : Entity.Factory<UmsRoleResourceRelation>()


    var id: Long

    //资源ID
    var resourceId: Long?

    //角色ID
    var roleId: Long?
}


//表定义： 后台角色资源关系表
open class UmsRoleResourceRelations(alias: String?) : Table<UmsRoleResourceRelation>("ums_role_resource_relation", alias) {
    companion object : UmsRoleResourceRelations(null)

    override fun aliased(alias: String) = UmsRoleResourceRelations(alias)


    val id = long("id").primaryKey()
            .bindTo { it.id }

    //资源ID
    val resourceId = long("resource_id").bindTo { it.resourceId }

    //角色ID
    val roleId = long("role_id").bindTo { it.roleId }
}
