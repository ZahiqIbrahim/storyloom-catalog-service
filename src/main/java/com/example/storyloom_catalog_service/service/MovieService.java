package com.example.storyloom_catalog_service.service;

import com.example.storyloom_catalog_service.external_clients.TmdbClient;
import com.example.storyloom_catalog_service.model.Book;
import com.example.storyloom_catalog_service.model.Movie;
import com.example.storyloom_catalog_service.repo.MoviesRepo;
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

    @Autowired
    private MoviesRepo moviesRepo;

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

        for (String movieTitle : movieTitles) {
            try {
                if (movieTitle == null || movieTitle.isBlank()) {
                    continue;
                }

                String cleanedTitle = movieTitle.trim().replace("\"", "");

                Map<String, Object> response = tmdbClient.searchMovie(cleanedTitle, apiKey);

                List<Map<String, Object>> results =
                        (List<Map<String, Object>>) response.get("results");

                if (results == null || results.isEmpty()) {
                    System.out.println("Movie not found: " + cleanedTitle);
                    continue; // skip this movie instead of crashing everything
                }

                Map<String, Object> firstMovie = results.get(0);

                Movie movie = new Movie();
                movie.setOverview((String) firstMovie.get("overview"));
                movie.setTitle((String) firstMovie.get("title"));
                movie.setReleaseDate(String.valueOf(firstMovie.get("release_date")));

                String posterPath = (String) firstMovie.get("poster_path");
                movie.setPosterPath(
                        posterPath != null ? "https://image.tmdb.org/t/p/w500" + posterPath : null
                );

                movie.setVoteAverage(String.valueOf(firstMovie.get("vote_average")));

                movies.add(movie);

            } catch (Exception e) {
                System.out.println("Failed to fetch movie: " + movieTitle + " -> " + e.getMessage());
                // skip failed movie and continue with the rest
            }
        }

        return movies;
    }

    public void getAndStoreTrendingMovies() {

        moviesRepo.deleteAll();

        List<Map<String, Object>> allMovies = new ArrayList<>();

        for(int page = 1; page <= 3; page++ ){

            Map<String, Object> response = tmdbClient.getTrendingMovies(apiKey, page);

            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

            allMovies.addAll(results);
        }



        if (allMovies == null || allMovies.isEmpty()) {
            throw new RuntimeException("Movies not found");
        }

        List<Movie> movies = new ArrayList<>();

        for(Map<String, Object> doc : allMovies){

            Movie movie = new Movie();
            movie.setTitle((String) doc.get("title"));
            movie.setOverview((String) doc.get("overview"));


            movie.setReleaseDate(String.valueOf( doc.get("release_date")));
            movie.setVoteAverage(String.valueOf( doc.get("vote_average")));

            String path = ((String) doc.get("poster_path"));

            if(path != null){
                movie.setPosterPath(  "https://image.tmdb.org/t/p/w500"
                        +  path
                        );
            }
            movies.add(movie);
        }

        moviesRepo.saveAll(movies);

    }


    public List<Movie> getTrendingMovies() {

        moviesRepo.deleteAll();
        return moviesRepo.findAll();
    }
}
