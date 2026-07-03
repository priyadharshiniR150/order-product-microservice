package com.example.order_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {
	    "com.example.order_service",
	    "com.example.common_module"
	})
	@EnableDiscoveryClient
	public class OrderServiceApplication {

	    public static void main(String[] args) {
	        SpringApplication.run(OrderServiceApplication.class, args);
	    }
	}