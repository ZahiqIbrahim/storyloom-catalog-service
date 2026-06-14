package com.example.storyloom_catalog_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class StoryloomCatalogServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StoryloomCatalogServiceApplication.class, args);
	}

}
