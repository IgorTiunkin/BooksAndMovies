package com.phantom.booksandmovies.exceptions;

public class MovieNotFoundException extends RuntimeException{
    public MovieNotFoundException() {
        super("Movie not found");
    }
}
