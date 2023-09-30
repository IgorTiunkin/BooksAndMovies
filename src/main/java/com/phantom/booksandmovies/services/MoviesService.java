package com.phantom.booksandmovies.services;

import com.phantom.booksandmovies.exceptions.MovieNotFoundException;
import com.phantom.booksandmovies.models.Movie;
import com.phantom.booksandmovies.models.MovieStatus;
import com.phantom.booksandmovies.repositories.MoviesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MoviesService {
    private final MoviesRepository moviesRepository;

    public List<Movie> getAllMovies() {
        return moviesRepository.findAll();
    }

    public Movie getMovieByTitle(String title) {
        return moviesRepository.findMovieByTitle(title).orElseThrow(MovieNotFoundException::new);
    }

    public List <Movie> getAllMoviesByStatus(MovieStatus movieStatus) {
        return moviesRepository.findAllByMovieStatus(movieStatus);
    }
}
