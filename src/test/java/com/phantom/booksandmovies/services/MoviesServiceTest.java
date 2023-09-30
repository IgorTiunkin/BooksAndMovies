package com.phantom.booksandmovies.services;

import com.phantom.booksandmovies.exceptions.MovieNotFoundException;
import com.phantom.booksandmovies.models.Movie;
import com.phantom.booksandmovies.models.MovieStatus;
import com.phantom.booksandmovies.repositories.MoviesRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class MoviesServiceTest {

    private MoviesService moviesService;
    private MoviesRepository moviesRepository;
    private final Movie BAKEMONOGATARI = new Movie(1, "Bakemonogatari", MovieStatus.WATCHED);
    private final Movie MONSTER = new Movie(2, "Monster", MovieStatus.DROPPED);
    private final Movie OSHI_NO_KO = new Movie(3, "Oshi no ko", MovieStatus.TO_WATCH);


    @BeforeEach
    public void init () {
        moviesRepository = mock(MoviesRepository.class);
        moviesService = new MoviesService(moviesRepository);
    }

    @Test
    public void contextLoad() {

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
        Assertions.assertEquals(3, testMovies.size());
    }

    @Test
    public void whenBakemonogatari_thenWatched() {
        final String TITLE = "Bakemonogatari";
        doReturn(Optional.of(BAKEMONOGATARI))
                .when(moviesRepository).findMovieByTitle(TITLE);
        Movie movie = moviesService.getMovieByTitle(TITLE);
        Assertions.assertEquals(MovieStatus.WATCHED, movie.getMovieStatus());
    }

    @Test
    public void whenMonster_ThenDropped() {
        final String TITLE = "Monster";
        doReturn(Optional.of(MONSTER))
                .when(moviesRepository).findMovieByTitle(TITLE);
        Movie movie = moviesService.getMovieByTitle(TITLE);
        Assertions.assertEquals(MovieStatus.DROPPED, movie.getMovieStatus());
    }

    @Test
    public void whenWatched_ThenSizeOne () {
        doReturn(List.of(BAKEMONOGATARI)).when(moviesRepository)
                .findAllByMovieStatus(MovieStatus.WATCHED);
        Assertions.assertEquals(1, moviesService.getAllMoviesByStatus(MovieStatus.WATCHED).size());
    }

    @Test
    public void whenBadTitle_ThenException() {
        String bad = "Bad";
        doReturn(Optional.empty()).when(moviesRepository).findMovieByTitle(bad);
        Assertions.assertThrows(MovieNotFoundException.class, () -> moviesService.getMovieByTitle(bad));
    }

}
