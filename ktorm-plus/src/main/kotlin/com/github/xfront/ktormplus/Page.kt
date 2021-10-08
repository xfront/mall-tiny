/*
 * Copyright (c) 2011-2021, baomidou (jobob@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.xfront.ktormplus

/**
 * 简单分页模型
 */
class Page<E>(
    override var current: Int = 1, //当前页
    override var size: Int = 10, //分页大小
    override var total: Long = 0 //总数
) : IPage<E> {
    /**
     * 查询数据列表
     */
    override var records: MutableList<E> = mutableListOf()

    /**
     * 排序字段信息
     */
    override var orders: MutableList<OrderItem> = mutableListOf()

    override var maxLimit: Int? = null

    override var countId: String? = null

    /**
     * 是否存在上一页
     */
    val hasPrevious get() = current > 1

    /**
     * 是否存在下一页
     */
    operator fun hasNext(): Boolean {
        return current < pages
    }

    /**
     * 添加新的排序条件，构造条件可以使用工厂：[OrderItem.build]
     *
     * @param items 条件
     * @return 返回分页参数本身
     */
    fun addOrder(vararg items: OrderItem): Page<E> {
        orders.addAll(listOf(*items))
        return this
    }

    /**
     * 添加新的排序条件，构造条件可以使用工厂：[OrderItem.build]
     *
     * @param items 条件
     * @return 返回分页参数本身
     */
    fun addOrder(items: List<OrderItem>): Page<E> {
        orders.addAll(items)
        return this
    }

    companion object {
        private const val serialVersionUID = 8545996863226528798L
        fun <E> of(current: Int, size: Int, total: Long = 0): Page<E> {
            return Page(current, size, total)
        }
    }
}