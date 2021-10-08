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
 * 后台角色资源关系表
 *
 *
 * @author macro
 * @since 2020-08-21
 */

@TableName("ums_role_resource_relation")
@ApiModel(value = "UmsRoleResourceRelation对象", description = "后台角色资源关系表")
class UmsRoleResourceRelation : Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    var id: Long = -1

    @ApiModelProperty(value = "角色ID")
    var roleId: Long = -1

    @ApiModelProperty(value = "资源ID")
    var resourceId: Long = -1

    companion object {
        private const val serialVersionUID = 1L
    }
}