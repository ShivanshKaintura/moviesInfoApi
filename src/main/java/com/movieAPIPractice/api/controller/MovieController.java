package com.movieAPIPractice.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movieAPIPractice.api.dto.MovieDto;
import com.movieAPIPractice.api.dto.MoviePageResponse;
import com.movieAPIPractice.api.exceptions.EmptyFileException;
import com.movieAPIPractice.api.service.MovieService;
import com.movieAPIPractice.api.utils.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/add-movie")
    public ResponseEntity<MovieDto> addMovieHandler(@RequestPart MultipartFile file, @RequestPart String movieDto) throws IOException, EmptyFileException {
        //MovieDto Mdt= convertToMovieDto(movieDto);
        if (file.isEmpty()) {
            throw new EmptyFileException("File is empty");
        }
        return new ResponseEntity<>(movieService.addMovie(convertToMovieDto(movieDto), file), HttpStatus.CREATED);

    }

    private MovieDto convertToMovieDto(String movieDtoObject) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(movieDtoObject, MovieDto.class);
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDto> getMovieHandler(@PathVariable Integer movieId) {
        return ResponseEntity.ok(movieService.getMovie(movieId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<MovieDto>> getAllMoviesHandler() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @PutMapping("/update/{movieId}")
    public ResponseEntity<MovieDto> updateMovieHandler(@PathVariable Integer movieId, @RequestPart MultipartFile multipartFile,
                                                       @RequestPart String movieDtoObj) throws IOException {

        if (multipartFile.isEmpty())
            multipartFile = null;

        return ResponseEntity.ok(movieService.updateMovie(movieId, convertToMovieDto(movieDtoObj), multipartFile));
    }


    @DeleteMapping("/delete/{movieId}")
    public ResponseEntity<String> deleteMovieHandler(@PathVariable Integer movieId) throws IOException {
        return ResponseEntity.ok(movieService.deleteMovie(movieId));
    }

    @GetMapping("/allMoviesPage")
    public ResponseEntity<MoviePageResponse> getMoviesWithPagination(
            @RequestParam(defaultValue = Constants.PAGE_NUMBER) Integer pageNo,
            @RequestParam(defaultValue = Constants.PAGE_SIZE, required = false) Integer pageSize
    ) {
        return ResponseEntity.ok(movieService.getAllMoviesWithPagination(pageNo, pageSize));
    }

    @GetMapping("/allMoviesPageSort")
    public ResponseEntity<MoviePageResponse> getMoviesWithPaginationAndSorting(
            @RequestParam(defaultValue = Constants.PAGE_NUMBER) Integer pageNo,
            @RequestParam(defaultValue = Constants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(defaultValue = Constants.SORT_BY, required = false) String sortBy,
            @RequestParam(defaultValue = Constants.SORT_DIRECTION, required = false) String direction
    ) {
        return ResponseEntity.ok(movieService.getAllMoviesWithPaginationAndSorting(pageNo, pageSize, sortBy, direction));
    }

}
