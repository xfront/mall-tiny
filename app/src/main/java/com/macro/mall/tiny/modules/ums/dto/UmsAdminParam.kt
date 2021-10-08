package com.macro.mall.tiny.modules.ums.dto

import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

/**
 * 用户登录参数
 * Created by macro on 2018/4/26.
 */
data class UmsAdminParam(
    @NotEmpty
    @ApiModelProperty(value = "用户名", required = true)
    var username:String? = null,

    @NotEmpty
    @ApiModelProperty(value = "密码", required = true)
    var password: String? = null,

    @ApiModelProperty(value = "用户头像")
    var icon: String? = null,

    @Email
    @ApiModelProperty(value = "邮箱")
    var email:String? = null,

    @ApiModelProperty(value = "用户昵称")
    var nickName: String? = null,

    @ApiModelProperty(value = "备注")
    var note: String? = null,
)