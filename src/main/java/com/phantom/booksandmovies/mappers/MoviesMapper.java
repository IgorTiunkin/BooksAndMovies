package com.phantom.booksandmovies.mappers;

import com.phantom.booksandmovies.DTO.MovieDTO;
import com.phantom.booksandmovies.models.Movie;
import org.springframework.stereotype.Component;

@Component
public class MoviesMapper {

    public MovieDTO mapToDTO(Movie movie) {
        return new MovieDTO(movie.getTitle(), movie.getMovieStatus());
    }
}
