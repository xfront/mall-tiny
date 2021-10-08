package com.macro.mall.tiny.common.api

import com.github.xfront.ktormplus.IPage


/**
 * 分页数据封装类
 * Created by macro on 2019/4/19.
 */
class CommonPage<T> {
    var pageNum: Int? = null
    var pageSize: Int? = null
    var totalPage: Int? = null
    var total: Long? = null
    var list: List<T>? = null

    companion object {
        /**
         * 将MyBatis Plus 分页结果转化为通用结果
         */
        fun <T> restPage(pageResult: IPage<T>): CommonPage<T> {
            val result = CommonPage<T>()
            result.pageNum = pageResult.current
            result.pageSize = pageResult.size
            result.total = pageResult.total
            result.totalPage = pageResult.pages
            result.list = pageResult.records
            return result
        }
    }
}