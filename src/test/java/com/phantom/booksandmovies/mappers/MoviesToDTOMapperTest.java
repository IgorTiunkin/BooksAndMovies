package com.phantom.booksandmovies.mappers;

import com.phantom.booksandmovies.DTO.MovieDTO;
import com.phantom.booksandmovies.models.Movie;
import com.phantom.booksandmovies.models.MovieStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class MoviesToDTOMapperTest {
    private final MoviesToDTOMapper moviesToDTOMapper = Mappers.getMapper(MoviesToDTOMapper.class);
    private final Movie BAKEMONOGATARI = new Movie(1, "Bakemonogatari", MovieStatus.WATCHED);

    @Test
    public void whenMovie_thenMovieDto () {
        MovieDTO movieDTO = moviesToDTOMapper.toMovieDTO(BAKEMONOGATARI);
        assertEquals(movieDTO.getTitle(), BAKEMONOGATARI.getTitle());
        assertEquals(movieDTO.getMovieStatus(), BAKEMONOGATARI.getMovieStatus());
    }

    @Test
    public void whenMovieDTO_thenMovie() {
        MovieDTO testMovieDTO = new MovieDTO(BAKEMONOGATARI.getTitle(), BAKEMONOGATARI.getMovieStatus());
        Movie movie = moviesToDTOMapper.toMovie(testMovieDTO);
        assertEquals(BAKEMONOGATARI.getTitle(), movie.getTitle());
        assertEquals(BAKEMONOGATARI.getMovieStatus(), movie.getMovieStatus());
    }

    @Test
    public void whenMovieNull_thenNull() {
        MovieDTO movieDTO = moviesToDTOMapper.toMovieDTO(null);
        assertNull(movieDTO);
    }

    @Test
    public void whenMovieDTONull_thenNull(){
        Movie movie = moviesToDTOMapper.toMovie(null);
        assertNull(movie);
    }
}
