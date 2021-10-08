package com.macro.mall.tiny.modules.ums.mapper

import com.github.xfront.ktormplus.KtormMapper
import com.macro.mall.tiny.modules.ums.model.UmsAdminLoginLog
import com.macro.mall.tiny.modules.ums.model.UmsAdminLoginLogs
import org.springframework.stereotype.Component

/**
 *
 *
 * 后台用户登录日志表 Mapper 接口
 *
 *
 * @author macro
 * @since 2020-08-21
 */
@Component
class UmsAdminLoginLogMapper : KtormMapper<UmsAdminLoginLog, UmsAdminLoginLogs>()