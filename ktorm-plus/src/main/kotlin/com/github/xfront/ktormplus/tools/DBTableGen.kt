package com.github.xfront.ktormplus.tools

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileWriter
import java.sql.DatabaseMetaData
import java.sql.ResultSet
import javax.sql.DataSource

@Component
class DBTableGen : ApplicationRunner {
    data class OutputInfo(val path: String, val pack: String)

    @Autowired
    lateinit var dataSource: DataSource

    @Autowired
    lateinit var ktorm: KtormGen

    override fun run(args: ApplicationArguments?) { //先获取jdbc连接
        val connection = dataSource.connection //从conn中获取数据库的元数据
        val projectPath = System.getProperty("user.dir")
        val outInfo = OutputInfo(projectPath, "com.macro.mall.tiny.modules.ums.model2")
        connection.use {
            val dbMetaData = connection.metaData //库信息
            val types = arrayOf("TABLE") //获取所有表
            val rsTable = dbMetaData.getTables(connection.catalog, null, null, types)
            while (rsTable.next()) {
                visitTable(outInfo.path, outInfo.pack, rsTable, dbMetaData, connection.catalog)
            }
        }
    }

    private fun visitTable(path: String, pack: String, rsTable: ResultSet, dbMetaData: DatabaseMetaData, catalog: String) {
        val tableName = rsTable.getString("TABLE_NAME") //表名
        val remarks = rsTable.getString("REMARKS") //表备注

        val dir = File(path, pack.toPath)
        dir.mkdirs()
        FileWriter(File(dir, "${tableName.toClassName}.kt"), false).use {
            ktorm.genKtorm(it, dbMetaData, tableName, catalog, pack, remarks)
        }
    }
}