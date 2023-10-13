package com.phantom.booksandmovies.integration.services;

import com.phantom.booksandmovies.exceptions.MovieNotFoundException;
import com.phantom.booksandmovies.models.Movie;
import com.phantom.booksandmovies.models.MovieStatus;
import com.phantom.booksandmovies.services.MoviesService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Sql({
        "classpath:sql/data.sql"
})
public class MovieServiceIT {
    private static final PostgreSQLContainer<?> container =
            new PostgreSQLContainer<>("postgres:13.1-alpine");

    @BeforeAll
    static void startContainer() {
        container.start();
    }

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
    }


    private final MoviesService moviesService;

    private final Movie BAKEMONOGATARI = Movie.builder()
            .id(1).title("Bakemonogatari").movieStatus(MovieStatus.WATCHED).build();
    private final Movie MONSTER = Movie.builder()
            .id(2).title("Monster").movieStatus(MovieStatus.DROPPED).build();
    private final Movie OSHI_NO_KO = Movie.builder()
            .id(3).title("Oshi no ko").movieStatus(MovieStatus.TO_WATCH).build();
    private final Movie BAD = Movie.builder()
            .id(7).title("Bad").movieStatus(MovieStatus.TO_WATCH).build();

    @Autowired
    public MovieServiceIT(MoviesService moviesService) {
        this.moviesService = moviesService;
    }

    @Test
    public void whenGetAll_thenSizeEqualsFive () {
        List<Movie> testMovies = moviesService.getAllMovies();
        assertEquals(5, testMovies.size());
    }

    @Test
    public void whenBakemonogatari_thenWatched() {
        Movie movie = moviesService.getMovieByTitle(BAKEMONOGATARI.getTitle());
        assertEquals(MovieStatus.WATCHED, movie.getMovieStatus());
    }

    @Test
    public void whenMonster_ThenDropped() {
        Movie movie = moviesService.getMovieByTitle(MONSTER.getTitle());
        assertEquals(MovieStatus.DROPPED, movie.getMovieStatus());
    }

    @Test
    public void whenWatched_ThenSizeThree () {
        assertEquals(3, moviesService.getAllMoviesByStatus(MovieStatus.WATCHED).size());
    }

    @Test
    public void whenBadTitle_ThenException() {
        String bad = "Bad";
        assertThrows(MovieNotFoundException.class, () -> moviesService.getMovieByTitle(bad));
    }

    @Test
    public void whenMovieIsAbsent_thenSave() {
        assertTrue(moviesService.insertMovie(BAD));
    }


    @Test
    public void whenMovieIsPresent_ThenDelete() {
        assertTrue(moviesService.deleteMovie(BAKEMONOGATARI.getTitle()));
    }

    @Test
    public void whenMovieIsAbsentInDelete_ThenException() {
        assertThrows(MovieNotFoundException.class,
                () -> moviesService.deleteMovie(BAD.getTitle()));
    }

    @Test
    public void whenUpdate_thenMovie() {
        assertEquals(MONSTER.getTitle(), moviesService.updateMovie(MONSTER, BAKEMONOGATARI.getTitle()).getTitle());
    }

    @Test
    public void whenMovieIsAbsentInUpdate_ThenException() {
        assertThrows(MovieNotFoundException.class,
                () -> moviesService.updateMovie(MONSTER, BAD.getTitle()));
    }


}
