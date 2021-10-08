package com.github.xfront.ktormplus

import okreflect.OkReflect
import org.ktorm.schema.BaseTable
import org.ktorm.schema.Column
import org.ktorm.schema.SqlType
import org.postgresql.PGStatement
import java.io.Serializable
import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.reflect.Field
import java.math.BigDecimal
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * 将值与枚举相互转换，不过也可用transform实现
 * val role = int("role").transform({ UserRole.fromCode(it) }, { it.code })
 * @author huangjf
 * @since 2021-10-09
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.ANNOTATION_CLASS)
annotation class EnumValue

/**
 * 自定义枚举接口
 */
interface IEnum<T : Serializable> {
    /**
     * 枚举数据库存储值
     */
    val value: T
}


/**
 * Define a column typed of [EnumXSqlType].
 *
 * @param name the column's name.
 * @return the registered column.
 */
inline fun <reified C : Enum<C>> BaseTable<*>.enumX(name: String): Column<C> {
    return registerColumn(name, EnumXSqlType(C::class.java))
}

/**
 * [SqlType] implementation that saves enums as int/string/short,etc.
 *
 * @property enumClass the enum class.
 */
class EnumXSqlType<C : Enum<C>>(private val enumClass: Class<C>) : SqlType<C>(Types.OTHER, "enum") {

    private val hasPostgresqlDriver by lazy {
        runCatching { Class.forName("org.postgresql.Driver") }.isSuccess
    }

    private val valueName: String

    init {
        valueName = if (IEnum::class.java.isAssignableFrom(enumClass)) "value"
        else enumClass.enumValueFieldName

        if (valueName.isBlank()) throw Exception("""Could not find @EnumValue in Class: ${enumClass}""")
    }

    override fun setParameter(ps: PreparedStatement, index: Int, parameter: C?) {
        if (parameter != null) {
            doSetParameter(ps, index, parameter)
        } else {
            if (hasPostgresqlDriver && ps.isWrapperFor(PGStatement::class.java)) {
                ps.setNull(index, Types.OTHER)
            } else {
                ps.setNull(index, Types.VARCHAR)
            }
        }
    }

    override fun doSetParameter(ps: PreparedStatement, index: Int, parameter: C) {
        val value = parameter.getValue(valueName)
        if (hasPostgresqlDriver && ps.isWrapperFor(PGStatement::class.java)) {
            ps.setObject(index, value, Types.OTHER)
        } else {
            when (value) {
                is Boolean -> ps.setBoolean(index, value)
                is Byte -> ps.setByte(index, value)
                is Char -> ps.setInt(index, value.code)
                is Short -> ps.setShort(index, value)
                is Int -> ps.setInt(index, value)
                is Long -> ps.setLong(index, value)
                is String -> ps.setString(index, value)
                is BigDecimal -> ps.setBigDecimal(index, value)
                else -> ps.setObject(index, value, Types.OTHER)
            }
        }
    }

    override fun doGetResult(rs: ResultSet, index: Int): C? {
        val value = rs.getObject(index) ?: return null
        return enumClass.valueOf(value)
    }

    private fun Class<C>.valueOf(value: Any): C? {
        return enumConstants.firstOrNull { equalsValue(value, it.getValue(valueName)) }
    }


    companion object {
        /**
         * 值比较
         *
         * @param sourceValue 数据库字段值
         * @param targetValue 当前枚举属性值
         * @return 是否匹配
         * @since 3.3.0
         */
        protected fun equalsValue(sourceValue: Any?, targetValue: Any?): Boolean {
            val sValue = sourceValue.toString()
            val tValue = targetValue.toString()
            return if (sourceValue is Number && targetValue is Number && BigDecimal(sValue).compareTo(BigDecimal(tValue)) == 0) {
                true
            } else sValue == tValue
        }

        /**
         * 查找标记标记EnumValue字段
         *
         * @param clazz class
         * @return EnumValue字段
         * @since 3.3.1
         */
        val Class<*>.enumValueFieldName: String
            get() {
                if (!isEnum) return ""

                return TABLE_METHOD_OF_ENUM_TYPES.computeIfAbsent(name) {
                    enumValueField?.name ?: ""
                }
            }

        private val Class<*>.enumValueField: Field?
            get() {
                return declaredFields.firstOrNull {
                    it.isAnnotationPresent(EnumValue::class.java)
                }
            }

        private fun Any.getValue(valueName: String): Any? {
            return try {
                OkReflect.onInstance(this)
                        .get(valueName)
            } catch (e: ReflectiveOperationException) {
                null
            }
        }

        fun Class<*>.isMpEnums(): Boolean {
            return isEnum && (IEnum::class.java.isAssignableFrom(this) || enumValueFieldName.isNotBlank())
        }

        private val TABLE_METHOD_OF_ENUM_TYPES: MutableMap<String, String> = ConcurrentHashMap()
    }
}
