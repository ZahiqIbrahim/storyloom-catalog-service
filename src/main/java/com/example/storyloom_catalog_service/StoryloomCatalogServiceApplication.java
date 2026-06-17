package com.example.storyloom_catalog_service;

import com.example.storyloom_catalog_service.service.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class StoryloomCatalogServiceApplication {
	@Autowired
	private BooksService booksService;

	public static void main(String[] args) {
		SpringApplication.run(StoryloomCatalogServiceApplication.class, args);
	}

	@Scheduled(cron = "0 0 0 * * MON", zone = "Asia/Kolkata")
	public void updateDb(){

		booksService.getAndStoreTrendingBooks();

	}

}
