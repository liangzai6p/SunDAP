package com.sunyard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author zgz
 */
@SpringBootApplication
@EnableFeignClients
public class SunOrmsSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SunOrmsSystemApplication.class, args);
	}

}
