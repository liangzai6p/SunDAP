package com.sunyard.dap;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @program: SunDAP
 * @description: Mybatis-plus代码生成器
 * @author: yey.he
 * @create: 2020-06-03 16:15
 **/
// 演示例子，执行 main 方法控制台输入模块表名回车自动生成对应项目目录中
@Slf4j
public class CodeGenerator {




    public static void main(String[] args) throws Exception {
        generate("dataserve",new String[]{"DM_MAC_TB"});

    }


    public static void generate(String module,String[] tableList)throws Exception{
        AutoGenerator mpg = new AutoGenerator();
        String projectPath  = System.getProperty("user.dir")+ File.separator+module;
        // TODO 此处务必记得修改
        String outputDir = projectPath+File.separator+"src"+File.separator+"main"+File.separator+"java";

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(outputDir);
        gc.setFileOverride(true);
        gc.setActiveRecord(true);
        gc.setDateType(DateType.ONLY_DATE);


        // XML 二级缓存
//        gc.setEnableCache(true);
        // XML ResultMap
        gc.setBaseResultMap(true);
        // XML columList
        gc.setBaseColumnList(true);
        gc.setAuthor("yey.he");

        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setMapperName("I%sMapper");
        gc.setXmlName("%sMapper");
        gc.setServiceName("I%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setControllerName("%sController");
        mpg.setGlobalConfig(gc);

        // 数据源配置mysql
//        DataSourceConfig dsc = new DataSourceConfig();
//        dsc.setDbType(DbType.MYSQL);
//        dsc.setDriverName("com.mysql.jdbc.Driver");
//        dsc.setUrl("jdbc:mysql://localhost:3306/itresources?useUnicode=true&amp;characterEncoding=UTF-8&amp;generateSimpleParameterMetadata=true");
//        dsc.setUsername("root");
//        dsc.setPassword("123456");
//        mpg.setDataSource(dsc);
        // 数据源配置oracle
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.ORACLE);
        dsc.setDriverName("oracle.jdbc.OracleDriver");
        dsc.setUrl("jdbc:oracle:thin:@172.1.1.11:1521/ORCL");
        dsc.setUsername("sundap");
        dsc.setPassword("123456");
        mpg.setDataSource(dsc);


        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // 表名生成策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        // 需要生成的表,大小写一定要正确
        strategy.setInclude(tableList);
        // 排除生成的表
//        strategy.setExclude(new String[]{"order"});
        Field field = strategy.getClass().getDeclaredField("logicDeleteFieldName");
        field.setAccessible(true);
        field.set(strategy, "logic_del");
        mpg.setStrategy(strategy);


        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.sunyard.dap."+module);
//        pc.setModuleName("dc");
        mpg.setPackageInfo(pc);


        // 执行生成
        mpg.execute();
        removeXML(module);

        log.info("自动构建完成！");
    }

    public static void removeXML(String module){
        String src = System.getProperty("user.dir")+File.separator+module
                +File.separator+"src"+File.separator+"main"+File.separator+"java"
                +File.separator+"com"+File.separator+"sunyard"+File.separator+"dap"
                +File.separator+module+File.separator+"mapper"+File.separator+"xml";
        String dest = System.getProperty("user.dir")+File.separator+module
                +File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"mapper";
        FileUtil.copyFilesFromDir(new File(src),new File(dest),true);
        FileUtil.del(src);
        log.info("xml移动完成");
    }

}
