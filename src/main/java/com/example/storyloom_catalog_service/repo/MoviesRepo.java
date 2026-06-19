package com.example.storyloom_catalog_service.repo;

import com.example.storyloom_catalog_service.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoviesRepo extends JpaRepository<Movie, Long> {
}
