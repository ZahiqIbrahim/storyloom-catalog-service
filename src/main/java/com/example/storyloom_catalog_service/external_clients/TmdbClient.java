package com.example.storyloom_catalog_service.external_clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;



@FeignClient( name = "tmdbClient", url = "https://api.themoviedb.org/3")
public interface TmdbClient {

    @GetMapping("/search/movie")
    Map<String, Object> searchMovie(@RequestParam("query") String query, @RequestParam("api_key") String apiKey);

    @GetMapping("/trending/movie/week")
    Map<String, Object> getTrendingMovies(@RequestParam("api_key") String apiKey);

}