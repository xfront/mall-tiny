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
 * 后台用户表
 *
 *
 * @author macro
 * @since 2020-08-21
 */

@TableName("ums_admin")
@ApiModel(value = "UmsAdmin对象", description = "后台用户表")
class UmsAdmin : Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    var id: Long = 0
    var username: String = ""
    var password: String? = null
    @ApiModelProperty(value = "头像")
    var icon: String? = null
    @ApiModelProperty(value = "邮箱")
    var email: String? = null
    @ApiModelProperty(value = "昵称")
    var nickName: String? = null
    @ApiModelProperty(value = "备注信息")
    var note: String? = null
    @ApiModelProperty(value = "创建时间")
    var createTime: Date? = null
    @ApiModelProperty(value = "最后登录时间")
    var loginTime: Date? = null
    @ApiModelProperty(value = "帐号启用状态：0->禁用；1->启用")
    var status: Int? = null


    override fun toString(): String {
        return """UmsAdmin{id=$id, username=$username, password=$password, icon=$icon, email=$email, nickName=$nickName, note=$note, createTime=$createTime, loginTime=$loginTime, status=$status}"""
    }
}