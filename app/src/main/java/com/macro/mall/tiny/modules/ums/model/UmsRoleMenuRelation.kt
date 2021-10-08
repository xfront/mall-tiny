package com.macro.mall.tiny.modules.ums.model

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.long


//实体类： 后台角色菜单关系表
interface UmsRoleMenuRelation : Entity<UmsRoleMenuRelation> {
    companion object : Entity.Factory<UmsRoleMenuRelation>()


    var id: Long

    //菜单ID
    var menuId: Long?

    //角色ID
    var roleId: Long?
}


//表定义： 后台角色菜单关系表
open class UmsRoleMenuRelations(alias: String?) : Table<UmsRoleMenuRelation>("ums_role_menu_relation", alias) {
    companion object : UmsRoleMenuRelations(null)

    override fun aliased(alias: String) = UmsRoleMenuRelations(alias)


    val id = long("id").primaryKey()
            .bindTo { it.id }

    //菜单ID
    val menuId = long("menu_id").bindTo { it.menuId }

    //角色ID
    val roleId = long("role_id").bindTo { it.roleId }
}
