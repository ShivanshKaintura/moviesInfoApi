package com.movieAPIPractice.SKMoviebhandar.repository;

import com.movieAPIPractice.SKMoviebhandar.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie,Integer> {

}
