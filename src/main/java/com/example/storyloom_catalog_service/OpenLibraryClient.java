package com.example.storyloom_catalog_service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "openLibraryClient", url = "https://openlibrary.org")
public interface    OpenLibraryClient {

    @GetMapping("/search.json")
    Map<String, Object> searchBook(@RequestParam("q") String query, @RequestParam("limit") int limit);

}
