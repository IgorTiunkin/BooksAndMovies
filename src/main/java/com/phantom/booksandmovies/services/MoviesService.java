package com.phantom.booksandmovies.services;

import com.phantom.booksandmovies.exceptions.MovieNotFoundException;
import com.phantom.booksandmovies.models.Movie;
import com.phantom.booksandmovies.models.MovieStatus;
import com.phantom.booksandmovies.repositories.MoviesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MoviesService {
    private final MoviesRepository moviesRepository;

    @Transactional (readOnly = true)
    public List<Movie> getAllMovies() {
        return moviesRepository.findAll();
    }

    @Transactional (readOnly = true)
    public Movie getMovieByTitle(String title) {
        return moviesRepository.findMovieByTitle(title).orElseThrow(MovieNotFoundException::new);
    }

    @Transactional (readOnly = true)
    public List <Movie> getAllMoviesByStatus(MovieStatus movieStatus) {
        return moviesRepository.findAllByMovieStatus(movieStatus);
    }

    @Transactional
    public boolean insertMovie(Movie movie) {
        Optional<Movie> movieByTitle = moviesRepository.findMovieByTitle(movie.getTitle());
        if (movieByTitle.isPresent()) return false;
        moviesRepository.save(movie);
        return true;
    }

    @Transactional
    public boolean deleteMovie(String title) {
        moviesRepository.delete(getMovieByTitle(title));
        return true;
    }

    @Transactional
    public Movie updateMovie(Movie movie, String oldTitle) {
        Movie movieByTitle = getMovieByTitle(oldTitle);
        movieByTitle.setTitle(movie.getTitle());
        movieByTitle.setMovieStatus(movie.getMovieStatus());
        moviesRepository.save(movieByTitle);
        return movie;
    }
}
