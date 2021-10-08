package com.macro.mall.tiny.modules.ums.model

import org.ktorm.entity.Entity
import org.ktorm.schema.*
import java.time.LocalDateTime


//实体类： 后台用户表
interface UmsAdmin : Entity<UmsAdmin> {
    companion object : Entity.Factory<UmsAdmin>()


    var id: Long

    //创建时间
    var createTime: LocalDateTime?

    //邮箱
    var email: String?

    //头像
    var icon: String?

    //最后登录时间
    var loginTime: LocalDateTime?

    //昵称
    var nickName: String?

    //备注信息
    var note: String?

    var password: String?

    //帐号启用状态：0->禁用；1->启用
    var status: Int?

    var username: String?
}


//表定义： 后台用户表
open class UmsAdmins(alias: String?) : Table<UmsAdmin>("ums_admin", alias) {
    companion object : UmsAdmins(null)

    override fun aliased(alias: String) = UmsAdmins(alias)


    val id = long("id").primaryKey()
            .bindTo { it.id }

    //创建时间
    val createTime = datetime("create_time").bindTo { it.createTime }

    //邮箱
    val email = varchar("email").bindTo { it.email }

    //头像
    val icon = varchar("icon").bindTo { it.icon }

    //最后登录时间
    val loginTime = datetime("login_time").bindTo { it.loginTime }

    //昵称
    val nickName = varchar("nick_name").bindTo { it.nickName }

    //备注信息
    val note = varchar("note").bindTo { it.note }

    val password = varchar("password").bindTo { it.password }

    //帐号启用状态：0->禁用；1->启用
    val status = int("status").bindTo { it.status }

    val username = varchar("username").bindTo { it.username }
}