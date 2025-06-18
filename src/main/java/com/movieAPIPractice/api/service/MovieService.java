package com.movieAPIPractice.api.service;


import com.movieAPIPractice.api.dto.MovieDto;
import com.movieAPIPractice.api.dto.MoviePageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MovieService {

    MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException;

    MovieDto getMovie(Integer movieId);

    List<MovieDto> getAllMovies();

    MovieDto updateMovie(Integer movieId, MovieDto movieDto, MultipartFile multipartFile) throws IOException;

    String deleteMovie(Integer movieId) throws IOException;


    MoviePageResponse getAllMoviesWithPagination(Integer pageNo, Integer pageSize);

    MoviePageResponse getAllMoviesWithPaginationAndSorting(Integer pageNo, Integer pageSize, String sortBy, String direction);

}
