package com.macro.mall.tiny.modules.ums.dto

import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotEmpty

/**
 * 修改用户名密码参数
 * Created by macro on 2019/10/9.
 */
data class UpdateAdminPasswordParam(
    @NotEmpty
    @ApiModelProperty(value = "用户名", required = true)
    var username: String = "",

    @NotEmpty
    @ApiModelProperty(value = "旧密码", required = true)
    var oldPassword: String = "",

    @NotEmpty
    @ApiModelProperty(value = "新密码", required = true)
    var newPassword: String = "",
)