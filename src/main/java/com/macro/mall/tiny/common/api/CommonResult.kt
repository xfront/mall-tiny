package com.macro.mall.tiny.common.api

/**
 * 通用返回对象
 * Created by macro on 2019/4/19.
 */
data class CommonResult<T>(
    var code: Long = 0,
    var message: String? = null,
    var data: T? = null
) {

    companion object {
        /**
         * 成功返回结果
         *
         * @param data 获取的数据
         * @param  message 提示信息
         */
        fun <T> success(data: T?, message: String? = null): CommonResult<T> {
            return CommonResult(ResultCode.SUCCESS.code, message, data)
        }

        /**
         * 失败返回结果
         * @param errorCode 错误码
         * @param message 错误信息
         */
        fun <T> failed(errorCode: IErrorCode, message: String? = null): CommonResult<T> {
            return CommonResult(errorCode.code, message, null)
        }

        /**
         * 失败返回结果
         * @param message 提示信息
         */
        fun <T> failed(message: String? = null): CommonResult<T> {
            return CommonResult(ResultCode.FAILED.code, message, null)
        }

        /**
         * 参数验证失败返回结果
         * @param message 提示信息
         */
        fun <T> validateFailed(message: String? = null): CommonResult<T> {
            return CommonResult(ResultCode.VALIDATE_FAILED.code, message, null)
        }

        /**
         * 未登录返回结果
         */
        fun <T> unauthorized(data: T? = null): CommonResult<T> {
            return CommonResult(ResultCode.UNAUTHORIZED.code, ResultCode.UNAUTHORIZED.message, data)
        }

        /**
         * 未授权返回结果
         */
        fun <T> forbidden(data: T? = null): CommonResult<T> {
            return CommonResult(ResultCode.FORBIDDEN.code, ResultCode.FORBIDDEN.message, data)
        }
    }
}