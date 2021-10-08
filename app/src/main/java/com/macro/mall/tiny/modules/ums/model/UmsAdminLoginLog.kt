package com.macro.mall.tiny.modules.ums.model

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.datetime
import org.ktorm.schema.long
import org.ktorm.schema.varchar
import java.time.LocalDateTime


//实体类： 后台用户登录日志表
interface UmsAdminLoginLog : Entity<UmsAdminLoginLog> {
    companion object : Entity.Factory<UmsAdminLoginLog>()


    var id: Long

    var address: String?

    var adminId: Long?

    var createTime: LocalDateTime?

    var ip: String?

    //浏览器登录类型
    var userAgent: String?
}


//表定义： 后台用户登录日志表
open class UmsAdminLoginLogs(alias: String?) : Table<UmsAdminLoginLog>("ums_admin_login_log", alias) {
    companion object : UmsAdminLoginLogs(null)

    override fun aliased(alias: String) = UmsAdminLoginLogs(alias)


    val id = long("id").primaryKey()
            .bindTo { it.id }

    val address = varchar("address").bindTo { it.address }

    val adminId = long("admin_id").bindTo { it.adminId }

    val createTime = datetime("create_time").bindTo { it.createTime }

    val ip = varchar("ip").bindTo { it.ip }

    //浏览器登录类型
    val userAgent = varchar("user_agent").bindTo { it.userAgent }
}
