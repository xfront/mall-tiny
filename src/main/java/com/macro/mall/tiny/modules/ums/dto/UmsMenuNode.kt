package com.macro.mall.tiny.modules.ums.dto

import com.macro.mall.tiny.modules.ums.model.UmsMenu
import io.swagger.annotations.ApiModelProperty

/**
 * 后台菜单节点封装
 * Created by macro on 2020/2/4.
 */
class UmsMenuNode : UmsMenu() {
    @ApiModelProperty(value = "子级菜单")
    var children: List<UmsMenuNode>? = null
}