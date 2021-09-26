package com.sunyard.dap.modelserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SunDapModelServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SunDapModelServerApplication.class, args);
    }
}
