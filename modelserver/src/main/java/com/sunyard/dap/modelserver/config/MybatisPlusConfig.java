package com.sunyard.dap.modelserver.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: SunDAP
 * @description: MybatisPlus配置
 * @author: yey.he
 * @create: 2020-06-03 16:02
 **/
@Configuration
@MapperScan("com.sunyard.dap.modelserver.mapper")
public class MybatisPlusConfig {
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        // 开启 count 的 join 优化,只针对 left join !!!
        // 启用分页插件
        return new PaginationInterceptor().setCountSqlParser(new JsqlParserCountOptimize(true));
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(){
        MapperScannerConfigurer scannerConfigurer = new MapperScannerConfigurer();
        //可以通过环境变量获取你的mapper路径,这样mapper扫描可以通过配置文件配置了
        scannerConfigurer.setBasePackage("com.sunyard.dap.modelserver.mapper");
        return scannerConfigurer;
    }



}
