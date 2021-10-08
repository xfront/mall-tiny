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
 * 后台菜单表
 *
 *
 * @author macro
 * @since 2020-08-21
 */

@TableName("ums_menu")
@ApiModel(value = "UmsMenu对象", description = "后台菜单表")
open class UmsMenu : Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    var id: Long = -1

    @ApiModelProperty(value = "父级ID")
    var parentId: Long = 0

    @ApiModelProperty(value = "创建时间")
    var createTime: Date? = null

    @ApiModelProperty(value = "菜单名称")
    var title: String? = null

    @ApiModelProperty(value = "菜单级数")
    var level: Int = 0

    @ApiModelProperty(value = "菜单排序")
    var sort: Int? = null

    @ApiModelProperty(value = "前端名称")
    var name: String? = null

    @ApiModelProperty(value = "前端图标")
    var icon: String? = null

    @ApiModelProperty(value = "前端隐藏")
    var hidden: Int? = null

    companion object {
        private const val serialVersionUID = 1L
    }
}