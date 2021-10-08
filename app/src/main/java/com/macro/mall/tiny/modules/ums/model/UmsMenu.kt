package com.macro.mall.tiny.modules.ums.model

import org.ktorm.entity.Entity
import org.ktorm.schema.*
import java.time.LocalDateTime


//实体类： 后台菜单表
interface UmsMenu : Entity<UmsMenu> {
    companion object : Entity.Factory<UmsMenu>()

    var children: List<UmsMenu>

    var id: Long

    //创建时间
    var createTime: LocalDateTime?

    //前端隐藏
    var hidden: Int?

    //前端图标
    var icon: String?

    //菜单级数
    var level: Int?

    //前端名称
    var name: String?

    //父级ID
    var parentId: Long?

    //菜单排序
    var sort: Int?

    //菜单名称
    var title: String?
}


//表定义： 后台菜单表
open class UmsMenus(alias: String?) : Table<UmsMenu>("ums_menu", alias) {
    companion object : UmsMenus(null)

    override fun aliased(alias: String) = UmsMenus(alias)


    val id = long("id").primaryKey()
            .bindTo { it.id }

    //创建时间
    val createTime = datetime("create_time").bindTo { it.createTime }

    //前端隐藏
    val hidden = int("hidden").bindTo { it.hidden }

    //前端图标
    val icon = varchar("icon").bindTo { it.icon }

    //菜单级数
    val level = int("level").bindTo { it.level }

    //前端名称
    val name = varchar("name").bindTo { it.name }

    //父级ID
    val parentId = long("parent_id").bindTo { it.parentId }

    //菜单排序
    val sort = int("sort").bindTo { it.sort }

    //菜单名称
    val title = varchar("title").bindTo { it.title }
}
