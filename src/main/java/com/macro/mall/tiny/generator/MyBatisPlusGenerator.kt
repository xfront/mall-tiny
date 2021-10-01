package com.macro.mall.tiny.generator

import cn.hutool.core.util.StrUtil
import cn.hutool.setting.dialect.Props
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException
import com.baomidou.mybatisplus.core.toolkit.StringPool
import com.baomidou.mybatisplus.generator.AutoGenerator
import com.baomidou.mybatisplus.generator.InjectionConfig
import com.baomidou.mybatisplus.generator.config.*
import com.baomidou.mybatisplus.generator.config.po.LikeTable
import com.baomidou.mybatisplus.generator.config.po.TableInfo
import com.baomidou.mybatisplus.generator.config.rules.DateType
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine
import java.util.*

/**
 * MyBatisPlus代码生成器
 * Created by macro on 2020/8/20.
 */
object MyBatisPlusGenerator {
    @JvmStatic
    fun main(args: Array<String>) {
        val projectPath = System.getProperty("user.dir")
        val moduleName = scanner("模块名")
        val tableNames = scanner("表名，多个英文逗号分割").split(",")
                .toTypedArray() // 代码生成器
        val autoGenerator = AutoGenerator()
        autoGenerator.globalConfig = initGlobalConfig(projectPath)
        autoGenerator.dataSource = initDataSourceConfig()
        autoGenerator.packageInfo = initPackageConfig(moduleName)
        autoGenerator.cfg = initInjectionConfig(projectPath, moduleName)
        autoGenerator.template = initTemplateConfig()
        autoGenerator.strategy = initStrategyConfig(tableNames)
        autoGenerator.templateEngine = VelocityTemplateEngine()
        autoGenerator.execute()
    }

    /**
     * 读取控制台内容信息
     */
    private fun scanner(tip: String): String {
        val scanner = Scanner(System.`in`)
        println("请输入$tip：")
        if (scanner.hasNext()) {
            val next = scanner.next()
            if (StrUtil.isNotEmpty(next)) {
                return next
            }
        }
        throw MybatisPlusException("请输入正确的$tip！")
    }

    /**
     * 初始化全局配置
     */
    private fun initGlobalConfig(projectPath: String): GlobalConfig {
        val globalConfig = GlobalConfig()
        globalConfig.isKotlin = true //关键点使用kotlin
        globalConfig.outputDir = "$projectPath/src/main/java"
        globalConfig.author = "macro"
        globalConfig.isOpen = false
        globalConfig.isSwagger2 = true
        globalConfig.isBaseResultMap = true
        globalConfig.isFileOverride = true
        globalConfig.dateType = DateType.ONLY_DATE
        globalConfig.entityName = "%s"
        globalConfig.mapperName = "%sMapper"
        globalConfig.xmlName = "%sMapper"
        globalConfig.serviceName = "%sService"
        globalConfig.serviceImplName = "%sServiceImpl"
        globalConfig.controllerName = "%sController"
        return globalConfig
    }

    /**
     * 初始化数据源配置
     */
    private fun initDataSourceConfig(): DataSourceConfig {
        val props = Props("generator.properties")
        val dataSourceConfig = DataSourceConfig()
        dataSourceConfig.url = props.getStr("dataSource.url")
        dataSourceConfig.driverName = props.getStr("dataSource.driverName")
        dataSourceConfig.username = props.getStr("dataSource.username")
        dataSourceConfig.password = props.getStr("dataSource.password")
        return dataSourceConfig
    }

    /**
     * 初始化包配置
     */
    private fun initPackageConfig(moduleName: String): PackageConfig {
        val props = Props("generator.properties")
        val packageConfig = PackageConfig()
        packageConfig.moduleName = moduleName
        packageConfig.parent = props.getStr("package.base")
        packageConfig.entity = "model"
        return packageConfig
    }

    /**
     * 初始化模板配置
     */
    private fun initTemplateConfig(): TemplateConfig {
        val templateConfig = TemplateConfig()
        //可以对controller、service、entity模板进行配置 //mapper.xml模板需单独配置
        templateConfig.xml = null
        return templateConfig
    }

    /**
     * 初始化策略配置
     */
    private fun initStrategyConfig(tableNames: Array<String>): StrategyConfig {
        val strategyConfig = StrategyConfig()
        strategyConfig.naming = NamingStrategy.underline_to_camel
        strategyConfig.columnNaming = NamingStrategy.underline_to_camel
        strategyConfig.isEntityLombokModel = true
        strategyConfig.isRestControllerStyle = true
        //当表名中带*号时可以启用通配符模式
        if (tableNames.size == 1 && tableNames[0].contains("*")) {
            val likeStr = tableNames[0].split("_")
                    .toTypedArray()
            val likePrefix = likeStr[0] + "_"
            strategyConfig.likeTable = LikeTable(likePrefix)
        } else {
            strategyConfig.setInclude(*tableNames)
        }
        return strategyConfig
    }

    /**
     * 初始化自定义配置
     */
    private fun initInjectionConfig(projectPath: String, moduleName: String): InjectionConfig { // 自定义配置
        val injectionConfig = object : InjectionConfig() {
            override fun initMap() {
            // 可用于自定义属性
            }
        }
        // 模板引擎是Velocity
        val templatePath = "/templates/mapper.xml.vm"
        // 自定义输出配置
        val focList = mutableListOf<FileOutConfig>()
        // 自定义配置会被优先输出
        focList.add(object : FileOutConfig(templatePath) {
            override fun outputFile(tableInfo: TableInfo): String {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return ("""$projectPath/src/main/resources/mapper/$moduleName/${tableInfo.entityName}Mapper${StringPool.DOT_XML}""")
            }
        })
        injectionConfig.fileOutConfigList = focList
        return injectionConfig
    }
}