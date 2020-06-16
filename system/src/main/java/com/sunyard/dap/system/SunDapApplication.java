package com.sunyard.dap.system;

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
public class SunDapApplication {
    public static void main(String[] args) {
        SpringApplication.run(SunDapApplication.class, args);
    }
}
