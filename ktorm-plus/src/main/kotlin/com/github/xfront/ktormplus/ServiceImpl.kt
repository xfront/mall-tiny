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
import org.ktorm.dsl.batchInsert
import org.ktorm.entity.Entity
import org.ktorm.entity.add
import org.ktorm.entity.single
import org.ktorm.entity.update
import org.ktorm.schema.ColumnDeclaring
import org.ktorm.schema.Table
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import java.io.Serializable

/**
 * IService 实现类（ 泛型：M 是 mapper 对象，E 是实体,T是表 ）
 *
 * @author huangjf
 * @since 2021-10-09
 */
open class ServiceImpl<M : KtormMapper<E, T>, E : Entity<E>, T : Table<E>> : IService<E, T> {
    @Autowired
    lateinit var mapper: M

    val entities get() = mapper.entities

    @Transactional(rollbackFor = [Exception::class])
    override fun saveBatch(entityList: Collection<E>, batchSize: Int): Boolean {
        var count = 0
        entityList.chunked(batchSize)
                .forEach { blist ->
                    count += entities.database.batchInsert(entities.sourceTable) {
                        for (obj in blist) {
                            item {
                                entities.sourceTable.columns.forEach { c ->
                                    val v = obj[c.name] ?: return@forEach
                                    set(c, v)
                                }
                            }
                        }
                    }.sum()
                }
        return count == entityList.size
    }

    override fun saveBatch(entityList: Collection<E>): Boolean {
        return saveBatch(entityList, IService.DEFAULT_BATCH_SIZE)
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun saveOrUpdate(entity: E): Boolean {
        val primaryKeys = entities.sourceTable.primaryKeys
        val hasPk = primaryKeys.any { entity[it.name] == null }
        if (!hasPk) entities.add(entity) else entities.update(entity)
        return true
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun saveOrUpdateBatch(entityList: Collection<E>, batchSize: Int): Boolean {
        for ((k, v) in entityList.groupBy { item ->
            val primaryKeys = entities.sourceTable.primaryKeys
            primaryKeys.any { item[it.name] == null }
        }) {
            if (!k) v.forEach { entities.add(it) }
            else v.forEach { entities.update(it) }
        }
        return true
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun updateBatchById(entityList: Collection<E>, batchSize: Int): Boolean {
        entityList.forEach { entities.update(it) }
        return true
    }

    override fun getOne(predicate: (T) -> ColumnDeclaring<Boolean>): E? {
        return entities.single(predicate)
    }

    override fun save(entity: E): Boolean {
        return mapper.insert(entity) > 0
    }

    override fun removeById(id: Serializable): Boolean {
        return mapper.deleteById(id) > 0
    }

    override fun remove(predicate: (T) -> ColumnDeclaring<Boolean>): Int {
        return mapper.removeIf(predicate)
    }

    override fun removeById(entity: E): Boolean {
        return mapper.deleteById(entity) > 0
    }

    override fun removeByMap(predicate: (T) -> ColumnDeclaring<Boolean>): Boolean {
        return mapper.removeIf(predicate) > 0
    }

    override fun removeByIds(idList: Collection<Serializable>): Boolean {
        return if (idList.isEmpty()) {
            false
        } else mapper.deleteBatchIds(idList) == idList.size
    }

    override fun updateById(entity: E): Boolean {
        return mapper.updateById(entity) > 0
    }

    override fun update(block: (UpdateStatementBuilder.(T) -> Unit)): Boolean {
        return update(null, block)
    }

    override fun update(entity: E?, block: (UpdateStatementBuilder.(T) -> Unit)?): Boolean {
        return mapper.update(entity, block) > 0
    }

    override fun getById(id: Serializable): E? {
        return mapper.selectById(id)
    }

    override fun listByIds(idList: Collection<Serializable>): List<E> {
        return mapper.selectBatchIds(idList)
    }

    override fun count(predicate: (T) -> ColumnDeclaring<Boolean>): Int {
        return mapper.selectCount(predicate)
    }

    override fun list(): MutableList<E> {
        return mapper.list(null)
    }

    override fun list(predicate: Predicate<T>): MutableList<E> {
        return mapper.list(predicate)
    }

    override fun page(page: IPage<E>, query: Query): IPage<E> {
        return mapper.selectPage(page, query)
    }

    override fun page(page: IPage<E>, predicate: (T) -> ColumnDeclaring<Boolean>): IPage<E> {
        return mapper.selectPage(page, predicate)
    }

    override fun saveOrUpdate(entity: E, block: UpdateStatementBuilder.(T) -> Unit): Boolean {
        return update(entity, block) || saveOrUpdate(entity)
    }

    override fun saveOrUpdateBatch(entityList: Collection<E>): Boolean {
        return saveOrUpdateBatch(entityList, IService.DEFAULT_BATCH_SIZE)
    }

    override fun updateBatchById(entityList: Collection<E>): Boolean {
        return updateBatchById(entityList, IService.DEFAULT_BATCH_SIZE)
    }
}