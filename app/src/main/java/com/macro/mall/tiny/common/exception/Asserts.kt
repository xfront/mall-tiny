package com.macro.mall.tiny.common.exception

import com.macro.mall.tiny.common.api.IErrorCode

/**
 * 断言处理类，用于抛出各种API异常
 * Created by macro on 2020/2/27.
 */
object Asserts {
    fun fail(message: String?) {
        throw ApiException(message)
    }

    fun fail(errorCode: IErrorCode) {
        throw ApiException(errorCode)
    }
}