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

import org.ktorm.dsl.Query
import org.ktorm.dsl.UpdateStatementBuilder
import org.ktorm.entity.Entity
import org.ktorm.schema.ColumnDeclaring
import org.ktorm.schema.Table
import java.io.Serializable

/**
 * 顶级 Service，（ 泛型：E 是实体,T是表 ）
 *
 * @author huangjf
 * @since 2021-10-09
 */
interface IService<E : Entity<E>, T : Table<E>> {
    fun save(entity: E): Boolean

    fun saveBatch(entityList: Collection<E>): Boolean

    fun saveBatch(entityList: Collection<E>, batchSize: Int): Boolean

    fun saveOrUpdate(entity: E): Boolean

    fun saveOrUpdate(entity: E, block: UpdateStatementBuilder.(T) -> Unit): Boolean

    fun saveOrUpdateBatch(entityList: Collection<E>): Boolean

    fun saveOrUpdateBatch(entityList: Collection<E>, batchSize: Int): Boolean

    fun removeById(id: Serializable): Boolean

    fun remove(predicate: (T) -> ColumnDeclaring<Boolean>): Int

    fun removeById(entity: E): Boolean

    fun removeByMap(predicate: (T) -> ColumnDeclaring<Boolean>): Boolean

    fun removeByIds(idList: Collection<Serializable>): Boolean

    fun updateById(entity: E): Boolean

    fun update(block: (UpdateStatementBuilder.(T) -> Unit)): Boolean

    fun update(entity: E?, block: (UpdateStatementBuilder.(T) -> Unit)?): Boolean

    fun updateBatchById(entityList: Collection<E>): Boolean

    fun updateBatchById(entityList: Collection<E>, batchSize: Int): Boolean

    fun count(predicate: (T) -> ColumnDeclaring<Boolean>): Int

    fun getOne(predicate: (T) -> ColumnDeclaring<Boolean>): E?

    fun getById(id: Serializable): E?

    fun list(): MutableList<E>

    fun list(predicate: Predicate<T>): MutableList<E>

    fun listByIds(idList: Collection<Serializable>): List<E>

    fun page(page: IPage<E>, query: Query): IPage<E>

    fun page(page: IPage<E>, predicate: (T) -> ColumnDeclaring<Boolean>): IPage<E>

    companion object {
        const val DEFAULT_BATCH_SIZE = 200
    }
}
