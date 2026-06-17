package com.example.storyloom_catalog_service.controller;

import com.example.storyloom_catalog_service.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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

    @GetMapping("/getMovies")
    public ResponseEntity<?> getMovies(@RequestBody List<String> movieTitles){
        try{
            return ResponseEntity.ok(movieService.getMovies(movieTitles));

        }catch (Exception e){
            return ResponseEntity.badRequest().body(Map.of("Error", e.getMessage()));
        }
    }
}
