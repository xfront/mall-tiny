package com.macro.mall.tiny.modules.ums.model

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.io.Serializable

/**
 *
 *
 * 后台角色菜单关系表
 *
 *
 * @author macro
 * @since 2020-08-21
 */
@TableName("ums_role_menu_relation")
@ApiModel(value = "UmsRoleMenuRelation对象", description = "后台角色菜单关系表")
class UmsRoleMenuRelation : Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    var id: Long = -1

    @ApiModelProperty(value = "角色ID")
    var roleId: Long = -1

    @ApiModelProperty(value = "菜单ID")
    var menuId: Long = -1

    companion object {
        private const val serialVersionUID = 1L
    }
}