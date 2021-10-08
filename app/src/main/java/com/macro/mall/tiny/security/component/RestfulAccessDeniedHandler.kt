package com.macro.mall.tiny.security.component

import cn.hutool.json.JSONUtil
import com.macro.mall.tiny.common.api.CommonResult
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 自定义返回结果：没有权限访问时
 * Created by macro on 2018/4/26.
 */
class RestfulAccessDeniedHandler : AccessDeniedHandler {
    @Throws(IOException::class, ServletException::class)
    override fun handle(
        request: HttpServletRequest, response: HttpServletResponse, e: AccessDeniedException
    ) {
        response.setHeader("Access-Control-Allow-Origin", "*")
        response.setHeader("Cache-Control", "no-cache")
        response.characterEncoding = "UTF-8"
        response.contentType = "application/json"
        response.writer.println(JSONUtil.parse(CommonResult.forbidden(e.message)))
        response.writer.flush()
    }
}