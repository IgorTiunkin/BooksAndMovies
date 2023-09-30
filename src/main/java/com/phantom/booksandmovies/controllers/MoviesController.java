package com.phantom.booksandmovies.controllers;

import com.phantom.booksandmovies.DTO.MovieDTO;
import com.phantom.booksandmovies.exceptions.MovieNotFoundException;
import com.phantom.booksandmovies.mappers.MoviesMapper;
import com.phantom.booksandmovies.models.Movie;
import com.phantom.booksandmovies.services.MoviesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/movies")
@RequiredArgsConstructor
public class MoviesController {
    private final MoviesService moviesService;
    private final MoviesMapper moviesMapper;

    @GetMapping("/all")
    public List <MovieDTO> getAllMovies() {
        List <Movie> movieList = moviesService.getAllMovies();
        List <MovieDTO> movieDTOS = movieList
                .stream()
                .map(moviesMapper::mapToDTO)
                .collect(Collectors.toList());
        return movieDTOS;
    }

    @GetMapping("/movie/{title}")
    public MovieDTO getMovieByTitle(@PathVariable("title") String title) {
        Movie movie = moviesService.getMovieByTitle(title);
        return moviesMapper.mapToDTO(movie);
    }

    @ExceptionHandler (MovieNotFoundException.class)
    public ResponseEntity<String> movieNotFound(MovieNotFoundException movieNotFoundException) {
        return new ResponseEntity<>(movieNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
