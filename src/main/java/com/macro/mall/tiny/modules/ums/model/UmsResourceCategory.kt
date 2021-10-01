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
 * 资源分类表
 *
 *
 * @author macro
 * @since 2020-08-21
 */
@TableName("ums_resource_category")
@ApiModel(value = "UmsResourceCategory对象", description = "资源分类表")
class UmsResourceCategory : Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    var id: Long = -1

    @ApiModelProperty(value = "创建时间")
    var createTime: Date? = null

    @ApiModelProperty(value = "分类名称")
    var name: String? = null

    @ApiModelProperty(value = "排序")
    var sort: Int? = null

    companion object {
        private const val serialVersionUID = 1L
    }
}