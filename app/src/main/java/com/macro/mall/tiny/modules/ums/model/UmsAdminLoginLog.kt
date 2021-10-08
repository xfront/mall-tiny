package com.macro.mall.tiny.modules.ums.model

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.io.Serializable
import java.util.*

/**
 *
 *
 * 后台用户登录日志表
 *
 *
 * @author macro
 * @since 2020-08-21
 */

@TableName("ums_admin_login_log")
@ApiModel(value = "UmsAdminLoginLog对象", description = "后台用户登录日志表")
data class UmsAdminLoginLog(
    @TableId(value = "id", type = IdType.AUTO)
    var id: Long? = null,
    var adminId: Long? = null,
    var createTime: Date? = null,
    var ip: String? = null,
    var address: String? = null,
    @ApiModelProperty(value = "浏览器登录类型")
    var userAgent: String? = null,
) : Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }
}