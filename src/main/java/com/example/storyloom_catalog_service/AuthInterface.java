package com.example.storyloom_catalog_service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient("STORYLOOM-AUTH-SERVICE")
public interface AuthInterface {

    @GetMapping("/hello")
    public ResponseEntity<String> hello();
}
