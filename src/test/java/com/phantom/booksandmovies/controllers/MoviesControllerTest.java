package com.phantom.booksandmovies.controllers;

import com.phantom.booksandmovies.DTO.MovieDTO;
import com.phantom.booksandmovies.exceptions.MovieNotFoundException;
import com.phantom.booksandmovies.mappers.MoviesToDTOMapper;
import com.phantom.booksandmovies.models.Movie;
import com.phantom.booksandmovies.models.MovieStatus;
import com.phantom.booksandmovies.services.MoviesService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MoviesControllerTest {

    private MoviesController moviesController;
    private MoviesToDTOMapper moviesToDTOMapper;
    private MoviesService moviesService;

    private final Movie BAKEMONOGATARI = new Movie(1, "Bakemonogatari", MovieStatus.WATCHED);
    private final Movie MONSTER = new Movie(2, "Monster", MovieStatus.DROPPED);
    private final Movie OSHI_NO_KO = new Movie(3, "Oshi no ko", MovieStatus.TO_WATCH);


    @BeforeEach
    public void init () {
        moviesService = mock(MoviesService.class);
        moviesToDTOMapper = Mappers.getMapper(MoviesToDTOMapper.class);
        moviesController = new MoviesController(moviesService, moviesToDTOMapper);
    }

    @Test
    public void whenAll_ThenSize3() {
        doReturn(List.of(BAKEMONOGATARI, MONSTER, OSHI_NO_KO))
                .when(moviesService).getAllMovies();
        assertEquals(3, moviesController.getAllMovies().size());
    }

    @Test
    public void whenAll_ThenDTO() {
        doReturn(List.of(BAKEMONOGATARI, MONSTER, OSHI_NO_KO))
                .when(moviesService).getAllMovies();
        assertEquals(MovieDTO.class, moviesController.getAllMovies().get(0).getClass());
    }


    @Test
    public void whenBakemonogatari_thenBakenomogatariDTO() {
        doReturn(BAKEMONOGATARI).when(moviesService).getMovieByTitle(BAKEMONOGATARI.getTitle());
        MovieDTO movieDTO = moviesController.getMovieByTitle(BAKEMONOGATARI.getTitle());
        assertEquals(BAKEMONOGATARI.getTitle(), movieDTO.getTitle());
        assertEquals(BAKEMONOGATARI.getMovieStatus(), movieDTO.getMovieStatus());
    }

    @Test
    public void whenBadTitle_ThenResponseEntity() {
        doThrow(new MovieNotFoundException()).when(moviesService).getMovieByTitle("Bad");
        assertThrows(MovieNotFoundException.class, () -> moviesController.getMovieByTitle("Bad"));
    }

    @Test
    public void whenDropped_ThenMonster() {
        doReturn(List.of(MONSTER)).when(moviesService).getAllMoviesByStatus(MovieStatus.DROPPED);
        assertEquals(MONSTER.getTitle(), moviesController.getMoviesByStatus(MovieStatus.DROPPED).get(0).getTitle());
    }


}
