package com.movieAPIPractice.api.repository;

import com.movieAPIPractice.api.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Integer> {

}
