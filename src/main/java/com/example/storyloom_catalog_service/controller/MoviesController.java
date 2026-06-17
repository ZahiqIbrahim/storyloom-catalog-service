package com.example.storyloom_catalog_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/movies")
public class MoviesController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/getMovie")
    public ResponseEntity<?> getMovie(@RequestBody String movieTitle){
        try{

            return ResponseEntity.ok(movieService.getMovie(movieTitle));

        }catch (Exception e){
            return ResponseEntity.badRequest().body(Map.of("Error", e.getMessage()));
        }
    }
}
