package com.phantom.booksandmovies.services;

import com.phantom.booksandmovies.exceptions.MovieNotFoundException;
import com.phantom.booksandmovies.models.Movie;
import com.phantom.booksandmovies.models.MovieStatus;
import com.phantom.booksandmovies.repositories.MoviesRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class MoviesServiceTest {

    private MoviesService moviesService;
    private MoviesRepository moviesRepository;

    private final Movie BAKEMONOGATARI = Movie.builder()
            .id(1).title("Bakemonogatari").movieStatus(MovieStatus.WATCHED).build();
    private final Movie MONSTER = Movie.builder()
            .id(2).title("Monster").movieStatus(MovieStatus.DROPPED).build();
    private final Movie OSHI_NO_KO = Movie.builder()
            .id(3).title("Oshi no ko").movieStatus(MovieStatus.TO_WATCH).build();


    @BeforeEach
    public void init () {
        moviesRepository = mock(MoviesRepository.class);
        moviesService = new MoviesService(moviesRepository);
    }

    @Test
    public void whenGetAll_thenSizeEqualsThree () {
        List <Movie> movieList = List.of(
                BAKEMONOGATARI,
                MONSTER,
                OSHI_NO_KO
        );
        doReturn(movieList).when(moviesRepository).findAll();
        List<Movie> testMovies = moviesService.getAllMovies();
        assertEquals(3, testMovies.size());
    }

    @Test
    public void whenBakemonogatari_thenWatched() {
        final String TITLE = "Bakemonogatari";
        doReturn(Optional.of(BAKEMONOGATARI))
                .when(moviesRepository).findMovieByTitle(TITLE);
        Movie movie = moviesService.getMovieByTitle(TITLE);
        assertEquals(MovieStatus.WATCHED, movie.getMovieStatus());
    }

    @Test
    public void whenMonster_ThenDropped() {
        final String TITLE = "Monster";
        doReturn(Optional.of(MONSTER))
                .when(moviesRepository).findMovieByTitle(TITLE);
        Movie movie = moviesService.getMovieByTitle(TITLE);
        assertEquals(MovieStatus.DROPPED, movie.getMovieStatus());
    }

    @Test
    public void whenWatched_ThenSizeOne () {
        doReturn(List.of(BAKEMONOGATARI)).when(moviesRepository)
                .findAllByMovieStatus(MovieStatus.WATCHED);
        assertEquals(1, moviesService.getAllMoviesByStatus(MovieStatus.WATCHED).size());
    }

    @Test
    public void whenBadTitle_ThenException() {
        String bad = "Bad";
        doReturn(Optional.empty()).when(moviesRepository).findMovieByTitle(bad);
        assertThrows(MovieNotFoundException.class, () -> moviesService.getMovieByTitle(bad));
    }

    @Test
    public void whenMovieIsAbsent_thenSave() {
        doReturn(Optional.empty()).when(moviesRepository).findMovieByTitle(BAKEMONOGATARI.getTitle());
        doReturn(null).when(moviesRepository).save(any());
        assertTrue(moviesService.insertMovie(BAKEMONOGATARI));
    }

    @Test
    public void whenMovieIsPresent_thenFalse() {
        doReturn(Optional.of(BAKEMONOGATARI)).when(moviesRepository).findMovieByTitle(BAKEMONOGATARI.getTitle());
        assertFalse(moviesService.insertMovie(BAKEMONOGATARI));
    }


    @Test
    public void whenMovieIsPresent_ThenDelete() {
        doNothing().when(moviesRepository).delete(any());
        doReturn(Optional.of(BAKEMONOGATARI)).when(moviesRepository).findMovieByTitle(BAKEMONOGATARI.getTitle());
        assertTrue(moviesService.deleteMovie(BAKEMONOGATARI.getTitle()));
    }

    @Test
    public void whenMovieIsAbsentInDelete_ThenException() {
        doReturn(Optional.empty()).when(moviesRepository).findMovieByTitle(BAKEMONOGATARI.getTitle());
        assertThrows(MovieNotFoundException.class,
                () -> moviesService.deleteMovie(BAKEMONOGATARI.getTitle()));
    }

    @Test
    public void whenUpdate_thenMovie() {
        doReturn(Optional.of(BAKEMONOGATARI)).when(moviesRepository).findMovieByTitle(BAKEMONOGATARI.getTitle());
        assertEquals(MONSTER, moviesService.updateMovie(MONSTER, BAKEMONOGATARI.getTitle()));
    }

    @Test
    public void whenMovieIsAbsentInUpdate_ThenException() {
        doReturn(Optional.empty()).when(moviesRepository).findMovieByTitle(BAKEMONOGATARI.getTitle());
        assertThrows(MovieNotFoundException.class,
                () -> moviesService.updateMovie(MONSTER, BAKEMONOGATARI.getTitle()));
    }

}
