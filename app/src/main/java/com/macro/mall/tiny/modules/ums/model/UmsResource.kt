package com.macro.mall.tiny.modules.ums.model

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.datetime
import org.ktorm.schema.long
import org.ktorm.schema.varchar
import java.time.LocalDateTime


//实体类： 后台资源表
interface UmsResource : Entity<UmsResource> {
    companion object : Entity.Factory<UmsResource>()


    var id: Long

    //资源分类ID
    var categoryId: Long?

    //创建时间
    var createTime: LocalDateTime?

    //描述
    var description: String?

    //资源名称
    var name: String?

    //资源URL
    var url: String?
}


//表定义： 后台资源表
open class UmsResources(alias: String?) : Table<UmsResource>("ums_resource", alias) {
    companion object : UmsResources(null)

    override fun aliased(alias: String) = UmsResources(alias)


    val id = long("id").primaryKey()
            .bindTo { it.id }

    //资源分类ID
    val categoryId = long("category_id").bindTo { it.categoryId }

    //创建时间
    val createTime = datetime("create_time").bindTo { it.createTime }

    //描述
    val description = varchar("description").bindTo { it.description }

    //资源名称
    val name = varchar("name").bindTo { it.name }

    //资源URL
    val url = varchar("url").bindTo { it.url }
}