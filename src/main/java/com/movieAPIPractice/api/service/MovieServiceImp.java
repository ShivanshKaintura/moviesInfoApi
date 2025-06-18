package com.movieAPIPractice.SKMoviebhandar.service;

import com.movieAPIPractice.SKMoviebhandar.dto.MovieDto;
import com.movieAPIPractice.SKMoviebhandar.dto.MoviePageResponse;
import com.movieAPIPractice.SKMoviebhandar.entities.Movie;
import com.movieAPIPractice.SKMoviebhandar.exceptions.FileExistsException;
import com.movieAPIPractice.SKMoviebhandar.exceptions.MovieNotFoundException;
import com.movieAPIPractice.SKMoviebhandar.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieServiceImp implements MovieService {

    private final MovieRepository movieRepository;
    private final FileService fileService;

    @Value("${project.poster}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;

    public MovieServiceImp(MovieRepository movieRepository, FileService fileService) {
        this.movieRepository = movieRepository;
        this.fileService = fileService;
    }

    @Override
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException {
        // 1. upload the file

        if(Files.exists(Paths.get(path+ File.separator+file.getOriginalFilename()))){
            throw new FileExistsException("File already exists, change file name");
        }
        String uploadedFileName = fileService.uploadFile(path,file);

        // 2. set the value of field poster as a file name
        movieDto.setPoster(uploadedFileName);

        // 3. map dto to movie object
        Movie movie=new Movie(
                null,
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );

        // 4. save the movie object -> saved Movie object

        /*save method adding and updating movies to DB
         if I give wrong id or null id in that case it will insert the record
         if I give id value which exists in the DB it will update the repository*/
        Movie savedMovie = movieRepository.save(movie);

        // 5. generate  poster url
        String posterUrl=baseUrl+"/file/"+uploadedFileName;
        
        // 6. map movie object to movieDto object
        MovieDto response=new MovieDto(
                savedMovie.getMovieId(),
                savedMovie.getTitle(),
                savedMovie.getDirector(),
                savedMovie.getStudio(),
                savedMovie.getMovieCast(),
                savedMovie.getReleaseYear(),
                savedMovie.getPoster(),
                posterUrl
        );
        return response;
    }

    @Override
    public MovieDto getMovie(Integer movieId) {
        //1. check data in DB if exists  get data of given id
        Movie movie = movieRepository.findById(movieId).orElseThrow(()-> new MovieNotFoundException("Movie Not Found with give id = "+movieId));

        // 2. Generate url
        String posterUrl=baseUrl+"/file/"+ movie.getPoster();

        //3. Map to MovieDto obj and return

        MovieDto response=new MovieDto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                posterUrl
        );

        return response;
    }

    @Override
    public List<MovieDto> getAllMovies() {
        //1. fetch all data from DB
        List<Movie> list=movieRepository.findAll();

        List<MovieDto> movieDtos=new ArrayList<>();

        //2. iterate through the list and generate poster url
        //map to movie dto object
        for(Movie movie:list){
            String posterUrl=baseUrl+"/file/"+ movie.getPoster();

            MovieDto response=new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
            movieDtos.add(response);
        }
        return movieDtos;
    }

    @Override
    public MovieDto updateMovie(Integer movieId, MovieDto movieDto, MultipartFile multipartFile) throws IOException {
        // 1. check if given id exists
        Movie movieExists = movieRepository.findById(movieId).orElseThrow(()-> new MovieNotFoundException("Movie Not Found with give id = "+movieId));

        //2. if movie obj exists update it
        // otherwise do nothing
        String fileName= movieExists.getPoster();
        if(multipartFile != null){
            Files.deleteIfExists(Paths.get(path+File.separator+fileName));
            fileName=fileService.uploadFile(path,multipartFile);
        }

        //3. set Dto poster value
        movieDto.setPoster(fileName);

        //4. map to movie obj
        Movie movie=new Movie(
                movieExists.getMovieId(),
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );

        //5. save movie obj & return it\
        Movie updatedMovie = movieRepository.save(movie);

        //6. generate url
        String posterUrl= baseUrl + "/file/" + fileName;

        //7. map to movieDto & return it

        return new MovieDto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                posterUrl
        );
    }

    @Override
    public String deleteMovie(Integer movieId) throws IOException {

        //1. check if movie obj exists in DB
        Movie exists=movieRepository.findById(movieId).orElseThrow(()-> new MovieNotFoundException("Movie Not Found with give id = "+movieId));

        //2.delete the file associated with given id
        Files.deleteIfExists(Paths.get(path+ File.separator+ exists.getPoster()));

        //3. delete the obj
        movieRepository.delete(exists);

        return "Movie deleted for this id = "+ movieId;
    }

    @Override
    public MoviePageResponse getAllMoviesWithPagination(Integer pageNo, Integer pageSize) {
        Pageable pageable= PageRequest.of(pageNo,pageSize);

        Page<Movie> moviePage=movieRepository.findAll(pageable);
        List<Movie> movies=moviePage.getContent();

        List<MovieDto> movieDtos=new ArrayList<>();

        //2. iterate through the list and generate poster url
        //map to movie dto object
        for(Movie movie:movies){
            String posterUrl=baseUrl+"/file/"+ movie.getPoster();

            MovieDto response=new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
            movieDtos.add(response);
        }
        return new MoviePageResponse(movieDtos,pageNo,pageSize,moviePage.getTotalPages(), (int) moviePage.getTotalElements(),moviePage.isLast());
    }

    @Override
    public MoviePageResponse getAllMoviesWithPaginationAndSorting(Integer pageNo, Integer pageSize, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable= PageRequest.of(pageNo,pageSize,sort);

        Page<Movie> moviePage=movieRepository.findAll(pageable);
        List<Movie> movies=moviePage.getContent();

        List<MovieDto> movieDtos=new ArrayList<>();

        //2. iterate through the list and generate poster url
        //map to movie dto object
        for(Movie movie:movies){
            String posterUrl=baseUrl+"/file/"+ movie.getPoster();

            MovieDto response=new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
            movieDtos.add(response);
        }
        return new MoviePageResponse(movieDtos
                ,pageNo
                ,pageSize
                ,moviePage.getTotalElements()
                ,moviePage.getTotalPages()
                ,moviePage.isLast());
    }
}
