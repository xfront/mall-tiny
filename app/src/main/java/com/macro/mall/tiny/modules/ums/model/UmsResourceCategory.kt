package com.macro.mall.tiny.modules.ums.model

import org.ktorm.entity.Entity
import org.ktorm.schema.*
import java.time.LocalDateTime


//实体类： 资源分类表
interface UmsResourceCategory : Entity<UmsResourceCategory> {
    companion object : Entity.Factory<UmsResourceCategory>()


    var id: Long

    //创建时间
    var createTime: LocalDateTime?

    //分类名称
    var name: String?

    //排序
    var sort: Int?
}


//表定义： 资源分类表
open class UmsResourceCategorys(alias: String?) : Table<UmsResourceCategory>("ums_resource_category", alias) {
    companion object : UmsResourceCategorys(null)

    override fun aliased(alias: String) = UmsResourceCategorys(alias)


    val id = long("id").primaryKey()
            .bindTo { it.id }

    //创建时间
    val createTime = datetime("create_time").bindTo { it.createTime }

    //分类名称
    val name = varchar("name").bindTo { it.name }

    //排序
    val sort = int("sort").bindTo { it.sort }
}
