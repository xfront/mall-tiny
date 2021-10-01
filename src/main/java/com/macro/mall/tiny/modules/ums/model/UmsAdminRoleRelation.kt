package com.macro.mall.tiny.modules.ums.model

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import io.swagger.annotations.ApiModel
import java.io.Serializable

/**
 *
 *
 * 后台用户和角色关系表
 *
 *
 * @author macro
 * @since 2020-08-21
 */

@TableName("ums_admin_role_relation")
@ApiModel(value = "UmsAdminRoleRelation对象", description = "后台用户和角色关系表")
data class UmsAdminRoleRelation(
    var adminId: Long? = null,
    var roleId: Long? = null,
) : Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    var id: Long = -1

    companion object {
        private const val serialVersionUID = 1L
    }
}