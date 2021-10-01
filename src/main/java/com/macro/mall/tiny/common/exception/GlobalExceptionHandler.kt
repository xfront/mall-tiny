package com.macro.mall.tiny.common.exception

import com.macro.mall.tiny.common.api.CommonResult
import org.springframework.validation.BindException
import org.springframework.validation.BindingResult
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

/**
 * 全局异常处理
 * Created by macro on 2020/2/27.
 */
@ControllerAdvice
class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(value = [ApiException::class])
    fun handle(e: ApiException): CommonResult<Any> {
        return if (e.errorCode != null) {
            CommonResult.failed(e.errorCode!!)
        } else CommonResult.failed(e.message)
    }

    @ResponseBody
    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun handleValidException(e: MethodArgumentNotValidException): CommonResult<Any> {
        return doValidate(e.bindingResult)
    }

    @ResponseBody
    @ExceptionHandler(value = [BindException::class])
    fun handleValidException(e: BindException): CommonResult<Any> {
        return doValidate(e.bindingResult)
    }

    private fun doValidate(bindingResult: BindingResult): CommonResult<Any> {
        var message: String? = null
        if (bindingResult.hasErrors()) {
            val fieldError = bindingResult.fieldError
            if (fieldError != null) {
                message = fieldError.field + fieldError.defaultMessage
            }
        }
        return CommonResult.validateFailed(message)
    }
}