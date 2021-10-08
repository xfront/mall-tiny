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

import java.io.Serializable

/**
 * 排序元素载体
 *
 * @author HCL
 * Create at 2019/5/27
 */

data class OrderItem(
    val column: String = "", //需要进行排序的字段
    val asc: Boolean = true, //是否正序排列，默认 true
) : Serializable {

    companion object {
        private const val serialVersionUID = 1L
        fun asc(column: String): OrderItem {
            return build(column, true)
        }

        fun desc(column: String): OrderItem {
            return build(column, false)
        }

        fun ascs(vararg columns: String): List<OrderItem> {
            return columns.map { asc(it) }
        }

        fun descs(vararg columns: String): List<OrderItem> {
            return columns.map { desc(it) }
        }

        private fun build(column: String, asc: Boolean): OrderItem {
            return OrderItem(column, asc)
        }
    }
}