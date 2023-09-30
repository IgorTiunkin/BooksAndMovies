package com.phantom.booksandmovies.controllers;

import com.phantom.booksandmovies.DTO.MovieDTO;
import com.phantom.booksandmovies.exceptions.MovieNotFoundException;
import com.phantom.booksandmovies.mappers.MoviesMapper;
import com.phantom.booksandmovies.models.Movie;
import com.phantom.booksandmovies.models.MovieStatus;
import com.phantom.booksandmovies.services.MoviesService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
public class MoviesControllerTest {

    private MoviesController moviesController;
    private MoviesMapper moviesMapper;
    private MoviesService moviesService;

    private final Movie BAKEMONOGATARI = new Movie(1, "Bakemonogatari", MovieStatus.WATCHED);
    private final Movie MONSTER = new Movie(2, "Monster", MovieStatus.DROPPED);
    private final Movie OSHI_NO_KO = new Movie(3, "Oshi no ko", MovieStatus.TO_WATCH);


    @BeforeEach
    public void init () {
        moviesService = mock(MoviesService.class);
        moviesMapper = new MoviesMapper();
        moviesController = new MoviesController(moviesService, moviesMapper);
    }

    @Test
    public void whenAll_ThenSize3() {
        doReturn(List.of(BAKEMONOGATARI, MONSTER, OSHI_NO_KO))
                .when(moviesService).getAllMovies();
        Assertions.assertEquals(3, moviesController.getAllMovies().size());
    }

    @Test
    public void whenAll_ThenDTO() {
        doReturn(List.of(BAKEMONOGATARI, MONSTER, OSHI_NO_KO))
                .when(moviesService).getAllMovies();
        Assertions.assertEquals(MovieDTO.class, moviesController.getAllMovies().get(0).getClass());
    }


    @Test
    public void whenBakemonogatari_thenBakenomogatariDTO() {
        doReturn(BAKEMONOGATARI).when(moviesService).getMovieByTitle(BAKEMONOGATARI.getTitle());
        MovieDTO movieDTO = moviesController.getMovieByTitle(BAKEMONOGATARI.getTitle());
        Assertions.assertEquals(BAKEMONOGATARI.getTitle(), movieDTO.getTitle());
        Assertions.assertEquals(BAKEMONOGATARI.getMovieStatus(), movieDTO.getMovieStatus());
    }

    @Test
    public void whenBadTitle_ThenResponseEntity() {
        doThrow(new MovieNotFoundException()).when(moviesService).getMovieByTitle("Bad");
        Assertions.assertThrows(MovieNotFoundException.class, () -> moviesController.getMovieByTitle("Bad"));
    }
}
