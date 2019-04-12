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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: rbc-es
 * @BelongsPackage: com.elens.data.rbces.mybatis
 * @Author: xuweichao
 * @CreateTime: 2019-04-10 17:08
 * @Description: 代码生成
 */
public class MysqlGenerator {

    public static void Generator(String[] tableName) {

        String projectPath = System.getProperty("user.dir");

        //============================== 全局配置
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
        //============================== 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig
                .setDbType(DbType.MYSQL)
                .setUrl("jdbc:mysql://172.16.199.40:3306/exchange1?useUnicode=true&characterEncoding=gbk&useSSL=false&serverTimezone=GMT%2B8")
                .setDriverName("com.mysql.cj.jdbc.Driver")
                .setUsername("root")
                //.setSchemaName("public")
                .setPassword("elensdata@zx123");
        //==============================包配置
        PackageConfig packageConfig = new PackageConfig();
        //配置父包路径
        packageConfig.setParent("com.elens.data.rbces")
//                配置业务包路径
//              .setModuleName("mybatis")
                .setMapper("mybatis.mapper")
                .setXml("mybatis.mapper")
                .setEntity("mybatis.entity")
                .setService("service")
                //会自动生成 impl，可以不设定
                .setServiceImpl("service.impl")
                .setController("controller");
        //============================== 自定义配置
        InjectionConfig injectionConfig = new InjectionConfig() {
            @Override
            public void initMap() {


            }
        };
        List<FileOutConfig> focList = new ArrayList<>();
//        focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                // 自定义输入文件名称
//                return projectPath + "/src/main/java/com/elens/data/rbces/mybatis/mapper"
////                        + pc.getModuleName()
//                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
//            }
//        });
//        cfg.setFileOutConfigList(focList);
        //============================== 策略配置
        StrategyConfig strategy = new StrategyConfig();
        //设置命名规则  underline_to_camel 底线变驼峰
        strategy.setNaming(NamingStrategy.underline_to_camel)
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
                .setTablePrefix(packageConfig.getModuleName() + "_");


        //============================== 生成配置
        AutoGenerator mpg = new AutoGenerator()
                .setCfg(injectionConfig)
                .setTemplate(new TemplateConfig().setXml(null))
                .setGlobalConfig(globalConfig)
                .setDataSource(dataSourceConfig)
                .setPackageInfo(packageConfig)
                .setStrategy(strategy)
//              选择 freemarker 引擎需要指定如下加，注意 pom 依赖必须有！
                .setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

    public static void main(String[] args) {
        Generator(new String[]{"city"});
    }
}
