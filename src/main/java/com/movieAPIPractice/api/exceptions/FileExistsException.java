package com.movieAPIPractice.SKMoviebhandar.exceptions;

public class FileExistsException  extends  RuntimeException{
    public FileExistsException(String message){
        super(message);
    }
}
