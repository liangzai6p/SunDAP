package com.sunyard.dap.dataserve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @program: SunDAP
 * @description: 系统管理启动类
 * @author: yey.he
 * @create: 2020-06-03 14:54
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class SunDapDataServeApplication {
    public static void main(String[] args) {
        SpringApplication.run(SunDapDataServeApplication.class, args);
    }
}
