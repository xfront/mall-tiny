package com.macro.mall.tiny.modules.ums.model

import org.ktorm.entity.Entity
import org.ktorm.schema.*
import java.time.LocalDateTime


//实体类： 后台用户角色表
interface UmsRole : Entity<UmsRole> {
    companion object : Entity.Factory<UmsRole>()


    var id: Long

    //后台用户数量
    var adminCount: Int?

    //创建时间
    var createTime: LocalDateTime?

    //描述
    var description: String?

    //名称
    var name: String?

    var sort: Int?

    //启用状态：0->禁用；1->启用
    var status: Int?
}


//表定义： 后台用户角色表
open class UmsRoles(alias: String?) : Table<UmsRole>("ums_role", alias) {
    companion object : UmsRoles(null)

    override fun aliased(alias: String) = UmsRoles(alias)


    val id = long("id").primaryKey()
            .bindTo { it.id }

    //后台用户数量
    val adminCount = int("admin_count").bindTo { it.adminCount }

    //创建时间
    val createTime = datetime("create_time").bindTo { it.createTime }

    //描述
    val description = varchar("description").bindTo { it.description }

    //名称
    val name = varchar("name").bindTo { it.name }

    val sort = int("sort").bindTo { it.sort }

    //启用状态：0->禁用；1->启用
    val status = int("status").bindTo { it.status }
}
