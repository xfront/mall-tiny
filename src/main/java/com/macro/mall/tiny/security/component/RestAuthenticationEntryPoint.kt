package com.macro.mall.tiny.security.component

import cn.hutool.json.JSONUtil
import com.macro.mall.tiny.common.api.CommonResult
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 自定义返回结果：未登录或登录过期
 * Created by macro on 2018/5/14.
 */
class RestAuthenticationEntryPoint : AuthenticationEntryPoint {
    @Throws(IOException::class, ServletException::class)
    override fun commence(request: HttpServletRequest, response: HttpServletResponse, authException: AuthenticationException) {
        response.setHeader("Access-Control-Allow-Origin", "*")
        response.setHeader("Cache-Control", "no-cache")
        response.characterEncoding = "UTF-8"
        response.contentType = "application/json"
        response.writer.println(JSONUtil.parse(CommonResult.unauthorized(authException.message)))
        response.writer.flush()
    }
}