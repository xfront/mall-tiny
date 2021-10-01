package com.macro.mall.tiny.common.exception

import com.macro.mall.tiny.common.api.IErrorCode

/**
 * 自定义API异常
 * Created by macro on 2020/2/27.
 */
class ApiException : RuntimeException {
    var errorCode: IErrorCode? = null
        private set

    constructor(errorCode: IErrorCode) : super(errorCode.message) {
        this.errorCode = errorCode
    }

    constructor(message: String?) : super(message) {}
    constructor(cause: Throwable?) : super(cause) {}
    constructor(message: String?, cause: Throwable?) : super(message, cause) {}
}