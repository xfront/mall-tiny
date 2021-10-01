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
 * 后台资源表
 *
 *
 * @author macro
 * @since 2020-08-21
 */
@TableName("ums_resource")
@ApiModel(value = "UmsResource对象", description = "后台资源表")
class UmsResource : Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    var id: Long = -1

    @ApiModelProperty(value = "创建时间")
    var createTime: Date? = null

    @ApiModelProperty(value = "资源名称")
    var name: String = ""

    @ApiModelProperty(value = "资源URL")
    var url: String = ""

    @ApiModelProperty(value = "描述")
    var description: String? = null

    @ApiModelProperty(value = "资源分类ID")
    var categoryId: Long? = null

    companion object {
        private const val serialVersionUID = 1L
    }
}