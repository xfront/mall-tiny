package com.github.xfront.ktormplus

import org.ktorm.dsl.*
import org.ktorm.entity.*
import org.ktorm.expression.ArgumentExpression
import org.ktorm.schema.*
import org.springframework.beans.factory.annotation.Autowired
import java.io.Serializable

typealias Predicate<T> = (T) -> ColumnDeclaring<Boolean>

fun <E : Any, T : BaseTable<E>> EntitySequence<E, T>.copy(): EntitySequence<E, T> = this.withExpression(expression.copy())

fun Iterable<ColumnDeclaring<Boolean>>.combineOrConditions(ifEmpty: Boolean = true): ColumnDeclaring<Boolean> {
    return this.reduceOrNull { a, b -> a or b } ?: ArgumentExpression(ifEmpty, BooleanSqlType)
}

interface KtormInterceptor {
    fun beforeInsert(e: Entity<*>)
    fun beforeUpdate(e: Entity<*>)
}

open class KtormMapper<E : Entity<E>, T : Table<E>> {
    @Autowired
    lateinit var entities: EntitySequence<E, out T>

    companion object { var globalInterceptor: KtormInterceptor? = null }

    fun insert(e: E): Int {
        globalInterceptor?.beforeInsert(e);
        return entities.add(e)
    }

    fun list(predicate: Predicate<T>? = null): MutableList<E> {
        return (if (predicate == null) entities.copy() else entities.filter(predicate)).toMutableList()
    }

    fun selectPage(page: IPage<E>, query: Query): IPage<E> {
        val total = query.totalRecords
        val rs = query.limit(page.offset, page.size)
                .map { entities.sourceTable.createEntity(it, false) }
                .toMutableList()
        val out: Page<E> = Page.of(page.current, page.size, total.toLong())
        out.records = rs
        return out
    }

    fun selectPage(page: IPage<E>, predicate: Predicate<T>? = null): IPage<E> {
        val tmp = (if (predicate == null) entities.copy() else entities.filter(predicate))
        val total = tmp.totalRecords
        val rs = tmp.drop(page.offset)
                .take(page.size)
                .toMutableList()
        val out: Page<E> = Page.of(page.current, page.size, total.toLong())
        out.records = rs
        return out
    }

    fun selectCount(predicate: Predicate<T>? = null): Int {
        return if (predicate == null) {
            entities.copy()
                    .count()
        } else {
            entities.count(predicate)
        }
    }

    fun selectBatchIds(idList: Collection<Serializable>): List<E> {
        return entities.filter { (it.primaryKeys[0] as Column<Serializable>).inList(idList) }
                .toList()
    }

    fun selectById(id: Serializable): E? {
        return entities.firstOrNull { (it.primaryKeys[0] as Column<Serializable>).eq(id) }
    }

    fun update(entity: E?, block: (UpdateStatementBuilder.(T) -> Unit)?): Int {
        return if (entity != null) {
            globalInterceptor?.beforeUpdate(entity);
            entities.update(entity)
        } else {
            entities.database.update(entities.sourceTable, block!!)
        }
    }

    fun updateById(entity: E): Int {
        globalInterceptor?.beforeUpdate(entity);
        return entities.update(entity)
    }

    fun deleteBatchIds(idList: Collection<Serializable>): Int {
        return entities.removeIf { (it.primaryKeys[0] as Column<Serializable>).inList(idList) }
    }

    fun deleteById(id: Serializable): Int {
        return entities.removeIf { (it.primaryKeys[0] as Column<Serializable>).eq(id) }
    }

    fun deleteById(entity: E): Int {
        return entity.delete()
    }

    fun removeIf(predicate: Predicate<T>? = null): Int {
        return if (predicate == null) {
            entities.database.deleteAll(entities.sourceTable)
        } else {
            entities.removeIf(predicate)
        }
    }
}