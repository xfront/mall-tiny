package com.github.xfront.ktormplus.tools

import org.springframework.stereotype.Component
import java.io.PrintWriter
import java.io.Writer
import java.sql.DatabaseMetaData
import java.sql.ResultSet

@Component
class KtormGen {

    data class ColumnInfo(
        val name: String, //列名
        val type: String, //类型
        val comment: String, //注释内容
        val isNullable: Boolean, //是否非空
        var isPrimeKey: Boolean = false //是否主键
    )

    fun genKtorm(w: Writer, metaData: DatabaseMetaData, tableName: String, catalog: String, pack: String, remarks: String) {
        val writer = PrintWriter(w)

        //1.读取列的基本信息
        val rsColumn = metaData.getColumns(catalog, metaData.userName, tableName, null)
        val columns = mutableListOf<ColumnInfo>()
        while (rsColumn.next()) {
            columns.add(parseColumn(rsColumn))
        }

        //2.更新主键信息
        val rsPKeys = metaData.getPrimaryKeys(catalog, null, tableName)
        while (rsPKeys.next()) {
            val cn = rsPKeys.getString("COLUMN_NAME")
            columns.find { it.name == cn }?.isPrimeKey = true
        }
        columns.sortBy { if (it.isPrimeKey) " " else it.name }

        //3.生成实体类及表定义
        writer.println("package ${pack}\n")
        imports.forEach { writer.println(it) }
        writeEntityClass(writer, tableName, remarks, columns)
        writeTableClass(writer, tableName, remarks, columns)
    }

    private fun writeTableClass(writer: PrintWriter, tableName: String, remarks: String, columns: MutableList<ColumnInfo>) {
        val className = tableName.toClassName //1.类注释
        writer.println("\n\n//表定义： ${remarks}") //2.类头定义
        writer.println("""open class ${className}s(alias: String?) : Table<${className}>("${tableName}", alias) {
                    |    companion object : ${className}s(null)
                    |    override fun aliased(alias: String) = ${className}s(alias)
                    |    """.trimMargin())

        //3.各列
        columns.forEach { //3.1列注释
            if (it.comment.isNotBlank()) writer.println("//${it.comment}") else writer.println() //3.2列定义
            writer.println(
                    """    val ${it.name.toFieldName} = ${it.type.toKtormType}("${it.name}")${if (it.isPrimeKey) ".primaryKey()" else ""}.bindTo { it.${it.name.toFieldName} }""")
        } //4.结束
        writer.println("}")
    }

    private fun writeEntityClass(writer: PrintWriter, tableName: String, remarks: String, columns: MutableList<ColumnInfo>) {
        val className = tableName.toClassName //1.类注释
        writer.println("\n\n//实体类： ${remarks}") //2.类头定义
        writer.println("""interface ${className} : Entity<${className}> {
    companion object : Entity.Factory<${className}>()
    """)

        //3.各列
        columns.forEach { //3.1列注释
            if (it.comment.isNotBlank()) writer.println("//${it.comment}") else writer.println() //3.2列定义
            writer.println("""    var ${it.name.toFieldName} : ${it.type.toKotlinType}${if (it.isNullable) "?" else ""}""")
        } //4.结束
        writer.println("}")
    }

    private fun parseColumn(rsColumn: ResultSet): ColumnInfo {
        val meta = mutableMapOf<String, String>()
        for (i in 0 until rsColumn.metaData.columnCount) {
            val key = rsColumn.metaData.getColumnName(i + 1)
            val value = rsColumn.getString(i + 1) ?: continue
            println("${key}:${value}")
            meta[key] = value
        }
        val nullFlag = meta["IS_NULLABLE"] ?: meta["NULLABLE"]
        val canNull = "1" == nullFlag || "YES" == nullFlag
        val columnName = rsColumn.getString("COLUMN_NAME")
        val type = rsColumn.getString("TYPE_NAME")
        val comment = rsColumn.getString("REMARKS")
        return ColumnInfo(columnName, type, comment, canNull)
    }

    companion object {
        val imports = arrayOf(
                "import org.ktorm.entity.Entity",
                "import org.ktorm.schema.*",
                "import java.math.BigDecimal",
                "import java.time.LocalDateTime",
                "import java.time.Instant",
        )

        val dbTypeMap = mapOf(
                "varchar" to Pair("varchar", "String"),
                "char" to Pair("varchar", "String"),
                "bigint" to Pair("long", "Long"),
                "bigint unsigned" to Pair("long", "Long"),
                "decimal" to Pair("decimal", "BigDecimal"),
                "bit" to Pair("short", "Short"),
                "short" to Pair("short", "Short"),
                "smartint" to Pair("short", "Short"),
                "tinyint" to Pair("short", "Short"),
                "int" to Pair("int", "Int"),
                "datetime" to Pair("datetime", "LocalDateTime"),
                "timestamp" to Pair("timestamp", "Instant"),
        )
    }
}

val String.toPath
    get() = this.replace('.', '/')

val String.toClassName
    get() = this.split("_")
            .joinToString("") { it.capitalize() }

val String.toFieldName
    get() = this.toClassName.decapitalize()

val String.toKtormType
    get() = KtormGen.dbTypeMap[lowercase()]?.first ?: "unknown"

val String.toKotlinType
    get() = KtormGen.dbTypeMap[lowercase()]?.second ?: "unknown"