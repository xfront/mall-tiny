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
 * 后台用户角色表
 *
 *
 * @author macro
 * @since 2020-08-21
 */
@TableName("ums_role")
@ApiModel(value = "UmsRole对象", description = "后台用户角色表")
class UmsRole : Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    var id: Long = -1

    @ApiModelProperty(value = "名称")
    var name: String? = null

    @ApiModelProperty(value = "描述")
    var description: String? = null

    @ApiModelProperty(value = "后台用户数量")
    var adminCount: Int? = null

    @ApiModelProperty(value = "创建时间")
    var createTime: Date? = null

    @ApiModelProperty(value = "启用状态：0->禁用；1->启用")
    var status: Int? = null
    var sort: Int? = null

    companion object {
        private const val serialVersionUID = 1L
    }
}