package com.sunyard.dap.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SunDapConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SunDapConsumerApplication.class, args);
    }

}
