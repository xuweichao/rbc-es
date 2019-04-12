package com.elens.data.rbces.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsProject: rbc-es
 * @BelongsPackage: com.elens.data.rbces.mybatis
 * @Author: xuweichao
 * @CreateTime: 2019-04-10 17:08
 * @Description: 代码生成
 */
public class MysqlGenerator {
    private static String projectPath = System.getProperty("user.dir");
    private static String parentPackageName = "com.elens.data.rbces";

    /**
     * 全局设置
     *
     * @return
     */
    public static GlobalConfig globalConfig() {

        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(projectPath + "/src/main/java")
                // 是否支持 AR
                .setActiveRecord(true)
                //设置作者名字
                .setAuthor("xuweichao")
                //文件覆盖(全新文件)
                .setFileOverride(true)
                //主键策略
                .setIdType(IdType.AUTO)
                //SQL 映射文件
                .setBaseResultMap(true)
                //SQL 片段
                .setBaseColumnList(true)
                .setSwagger2(true)
                .setEnableCache(false)
                .setOpen(false)
                //时间类型
                .setDateType(DateType.ONLY_DATE)
                .setEnableCache(false)
        ;
        return globalConfig;
    }

    /**
     * 数据源配置
     *
     * @return
     */
    public static DataSourceConfig dataSourceConfig() {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig
                .setDbType(DbType.MYSQL)
                .setUrl("jdbc:mysql://172.16.199.40:3306/exchange1?useUnicode=true&characterEncoding=gbk&useSSL=false&serverTimezone=GMT%2B8")
                .setDriverName("com.mysql.cj.jdbc.Driver")
                .setUsername("root")
                //.setSchemaName("public")
                .setPassword("elensdata@zx123");
        return dataSourceConfig;
    }

    /**
     * 包名相关配置
     *
     * @return
     */
    public static PackageConfig packageConfig(String moduleName) {
        PackageConfig packageConfig = new PackageConfig();
        //配置父包路径
        packageConfig.setParent(parentPackageName)

                .setMapper("mybatis.mapper")
                .setXml("mybatis.mapper")
                .setEntity("mybatis.entity")
                .setService("service")
                //会自动生成 impl，可以不设定
                .setServiceImpl("service.impl")
                .setController("controller");

        if (StringUtils.isNotEmpty(moduleName)) {
            //配置业务包路径
            packageConfig.setModuleName(moduleName);
        }

        return packageConfig;
    }

    /**
     * 配置模板
     *
     * @return
     */
    public static InjectionConfig injectionConfig(String moduleName) {
        InjectionConfig injectionConfig = new InjectionConfig() {
            //自定义属性注入:abc
            //在.ftl(或者是.vm)模板中，通过${cfg.abc}获取属性
            @Override
            public void initMap() {
//                Map<String, Object> map = new HashMap<>();
//                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
//                this.setMap(map);
            }
        };

//        // 自定义输出配置
//        List<FileOutConfig> focList = new ArrayList<>();
//        // 如果模板引擎是 freemarker
//        String templatePath = "/templates/mapper.xml.ftl";
//        // 如果模板引擎是 velocity
//        // String templatePath = "/templates/mapper.xml.vm";
//
//        // 自定义配置会被优先输出
//        focList.add(new FileOutConfig(templatePath) {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
//                return projectPath + "/src/main/resources/mapper/" + moduleName
//                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
//            }
//        });

        return injectionConfig;
    }

    /**
     * 生成策略配置
     *
     * @param tableName
     * @return
     */
    public static StrategyConfig strategyConfig(String moduleName, String... tableName) {
        StrategyConfig strategyConfig = new StrategyConfig();
        //设置命名规则  underline_to_camel 底线变驼峰
        strategyConfig.setNaming(NamingStrategy.underline_to_camel)
                //设置设置列命名  underline_to_camel 底线变驼峰
                .setColumnNaming(NamingStrategy.underline_to_camel)
                //设置继承类
                //.setSuperEntityClass("com.maoxs.pojo")
                //设置继承类
                //.setSuperControllerClass("com.maoxs.controller")
                //是否加入lombok
                .setEntityLombokModel(true)
                .setRestControllerStyle(true)
                .setControllerMappingHyphenStyle(true)
                //设置表名
                .setInclude(tableName)
                //设置超级列
//                .setSuperEntityColumns("id")
                //设置controller映射联字符
                .setControllerMappingHyphenStyle(true)
                //表的前缀
                .setTablePrefix(packageConfig(moduleName).getModuleName() + "_");
        return strategyConfig;
    }

    /**
     * 配置模板
     *
     * @return
     */
    public static TemplateConfig templateConfig() {
        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        // templateConfig.setEntity("templates/entity2.java");
        // templateConfig.setService();
        templateConfig.setController("/templates/controller.java");

        templateConfig.setXml(null);
        return templateConfig;
    }

    public static void Generator(String moduleName, String... tableName) {
        AutoGenerator mpg = new AutoGenerator()
                .setCfg(injectionConfig(moduleName))
                .setTemplate(templateConfig())
                .setGlobalConfig(globalConfig())
                .setDataSource(dataSourceConfig())
                .setPackageInfo(packageConfig(moduleName))
                .setStrategy(strategyConfig(moduleName, tableName));
//              选择 freemarker 引擎需要指定如下加，注意 pom 依赖必须有！默认 Veloctiy
//                .setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

    public static void main(String[] args) {
        Generator(null, new String[]{"elens_report"});
    }
}
