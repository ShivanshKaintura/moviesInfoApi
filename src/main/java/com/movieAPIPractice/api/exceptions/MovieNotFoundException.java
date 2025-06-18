package com.movieAPIPractice.SKMoviebhandar.exceptions;

public class MovieNotFoundException extends RuntimeException{

    public MovieNotFoundException(String message){
        super(message);
    }
}
