package com.github.xfront.ktormplus

import java.io.Serializable
import kotlin.math.max

/**
 * 分页 Page 对象接口
 *
 * @author hubin
 * @since 2018-06-09
 */
interface IPage<E> : Serializable {
    /**
     * 获取排序信息，排序的字段和正反序
     *
     * @return 排序信息
     */
    var orders: MutableList<OrderItem>

    /**
     * 计算当前分页偏移量
     */
    val offset: Int
        get() {
            val current = current
            return if (current <= 1L) 0
            else max((current - 1) * size, 0)
        }

    /**
     * 最大每页分页数限制,优先级高于分页插件内的 maxLimit
     *
     * @since 3.4.0 @2020-07-17
     */
    var maxLimit: Int?

    var countId: String?

    /**
     * 当前分页总页数
     */
    var pages: Int
        get() {
            if (size == 0) {
                return 0
            }
            var pages = total / size
            if (total % size != 0L) {
                pages++
            }
            return pages.toInt()
        }
        set(value) {
        }

    /**
     * 分页记录列表
     *
     * @return 分页对象记录列表
     */
    var records: MutableList<E>

    /**
     * 当前满足条件总行数
     *
     * @return 总条数
     */
    var total: Long


    /**
     * 获取每页显示条数
     *
     * @return 每页显示条数
     */
    var size: Int


    /**
     * 当前页
     *
     * @return 当前页
     */
    var current: Int

    /**
     * 老分页插件不支持
     *
     *
     * MappedStatement 的 id
     *
     * @return id
     * @since 3.4.0 @2020-06-19
     */
    fun countId(): String? {
        return null
    }
}
