package com.example.storyloom_catalog_service.service;

import com.example.storyloom_catalog_service.external_clients.TmdbClient;
import com.example.storyloom_catalog_service.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MovieService {

    @Autowired
    private TmdbClient tmdbClient;

    @Value("${tmdb.api.key}")
    private String apiKey;

    public Movie getMovie(String movieTitle) {

        Movie movie = new Movie();

        Map<String, Object> response = tmdbClient.searchMovie(movieTitle, apiKey );

        List<Map<String, Object>>  results =  (List<Map<String, Object>>) response.get("results");

        if (results.isEmpty()) {
            throw new RuntimeException("Movie not found");
        }
        Map<String, Object> firstMovie = results.get(0);

        movie.setOverview((String) firstMovie.get("overview"));
        movie.setTitle((String) firstMovie.get("title"));
        movie.setReleaseDate(String.valueOf(firstMovie.get("release_date")));
        movie.setPosterPath("https://image.tmdb.org/t/p/w500"+ (String) firstMovie.get("poster_path")); //https://image.tmdb.org/t/p/w500/IYUD7rAIXzBM91TT3Z5fILUS7n.jpg
        movie.setVoteAverage(String.valueOf(firstMovie.get("vote_average")));

        return movie;
    }

    public List<Movie> getMovies(List<String> movieTitles) {

        List<Movie> movies = new ArrayList<>();

        for(String movieTitle : movieTitles){

            Movie movie = new Movie();

            Map<String, Object> response = tmdbClient.searchMovie(movieTitle, apiKey );

            List<Map<String, Object>>  results =  (List<Map<String, Object>>) response.get("results");

            if (results.isEmpty()) {
                throw new RuntimeException("Movie not found");
            }
            Map<String, Object> firstMovie = results.get(0);

            movie.setOverview((String) firstMovie.get("overview"));
            movie.setTitle((String) firstMovie.get("title"));
            movie.setReleaseDate(String.valueOf(firstMovie.get("release_date")));
            movie.setPosterPath("https://image.tmdb.org/t/p/w500"+ (String) firstMovie.get("poster_path")); //https://image.tmdb.org/t/p/w500/IYUD7rAIXzBM91TT3Z5fILUS7n.jpg
            movie.setVoteAverage(String.valueOf(firstMovie.get("vote_average")));

            movies.add(movie);

        }

      return movies;
    }
}
