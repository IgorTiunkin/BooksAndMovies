package com.phantom.booksandmovies.controllers;

import com.phantom.booksandmovies.DTO.MovieDTO;
import com.phantom.booksandmovies.exceptions.MovieNotFoundException;
import com.phantom.booksandmovies.mappers.MoviesToDTOMapper;
import com.phantom.booksandmovies.models.Movie;
import com.phantom.booksandmovies.models.MovieStatus;
import com.phantom.booksandmovies.services.MoviesService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/movies")
@RequiredArgsConstructor
public class MoviesController {
    private final MoviesService moviesService;
    private final MoviesToDTOMapper moviesToDTOMapper;

    @GetMapping("/all")
    @ApiOperation(value = "Get all movies",
    notes = "You can get all movies with all statuses")
    public List <MovieDTO> getAllMovies() {
        List <Movie> movieList = moviesService.getAllMovies();
        List <MovieDTO> movieDTOS = movieList
                .stream()
                .map(moviesToDTOMapper::toMovieDTO)
                .collect(Collectors.toList());
        return movieDTOS;
    }

    @GetMapping("/movie/{title}")
    @ApiOperation(value = "Get movie by title",
    notes = "Give the title of movie and get it",
    response = MovieDTO.class)
    public MovieDTO getMovieByTitle(@PathVariable("title") String title) {
        Movie movie = moviesService.getMovieByTitle(title);
        return moviesToDTOMapper.toMovieDTO(movie);
    }


    @ExceptionHandler (MovieNotFoundException.class)
    public ResponseEntity<String> movieNotFound(MovieNotFoundException movieNotFoundException) {
        return new ResponseEntity<>(movieNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/all/{status}")
    @ApiOperation(value = "Get all movies by status")
    public List <MovieDTO> getMoviesByStatus (@PathVariable ("status") MovieStatus movieStatus) {
        List<Movie> allMoviesByStatus = moviesService.getAllMoviesByStatus(movieStatus);
        List <MovieDTO> movieDTOList = allMoviesByStatus
                .stream().map(moviesToDTOMapper::toMovieDTO)
                .collect(Collectors.toList());
        return movieDTOList;
    }

    @ExceptionHandler ({ConversionFailedException.class, HttpMessageNotReadableException.class})
    public ResponseEntity <String> statusNotExist () {
        return new ResponseEntity<>(String.format("Status not exist. Available statuses: %s",
                Arrays.toString(MovieStatus.values())),
                HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/insert")
    @ApiOperation(value = "Insert new movie")
    public ResponseEntity <String> insertMovie(@RequestBody @Valid MovieDTO movieDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(String.format("Insert error. Name cannot be empty. Available statuses: %s",
                    Arrays.toString(MovieStatus.values())),
                    HttpStatus.BAD_REQUEST);
        }
        Movie movie = moviesToDTOMapper.toMovie(movieDTO);
        boolean isInserted = moviesService.insertMovie(movie);
        if (isInserted) return new ResponseEntity<>(String.format("Movie saved: title: %s , status: %s",
                movie.getTitle(), movie.getMovieStatus()), HttpStatus.CREATED);
        return new ResponseEntity<>("Movie with such title already exist. If you want to update use /title/update",
                HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{title}")
    @ApiOperation(value = "Delete movie")
    public ResponseEntity <String> deleteMovie (@PathVariable ("title") String title) {
        moviesService.deleteMovie(title);
        return new ResponseEntity<>(String.format("Movie %s successfully deleted", title),
        HttpStatus.OK);
    }

    @PutMapping ("/movie/{title}")
    @ApiOperation(value = "Update movie")
    public ResponseEntity <String> updateMovie (@PathVariable ("title") String oldTitle,
                                                @RequestBody @Valid MovieDTO movieDTO,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(String.format("Update error. Name cannot be empty. Available statuses: %s",
                    Arrays.toString(MovieStatus.values())),
                    HttpStatus.BAD_REQUEST);
        }

        Movie movie = moviesToDTOMapper.toMovie(movieDTO);
        moviesService.updateMovie(movie, oldTitle);
        return new ResponseEntity<>(String.format("Movie successfully updated. Title: %s, Status: %s",
                movie.getTitle(),
                movie.getMovieStatus()),
                HttpStatus.OK);

    }

}
