package com.phantom.booksandmovies.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phantom.booksandmovies.DTO.MovieDTO;
import com.phantom.booksandmovies.exceptions.MovieNotFoundException;
import com.phantom.booksandmovies.mappers.MoviesToDTOMapper;
import com.phantom.booksandmovies.models.Movie;
import com.phantom.booksandmovies.models.MovieStatus;
import com.phantom.booksandmovies.services.MoviesService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MoviesControllerTest {


    private MockMvc mvc;

    private MoviesController moviesController;
    private MoviesToDTOMapper moviesToDTOMapper;
    private MoviesService moviesService;
    private ObjectMapper objectMapper;

    private final Movie BAKEMONOGATARI = new Movie(1, "Bakemonogatari", MovieStatus.WATCHED);
    private final Movie MONSTER = new Movie(2, "Monster", MovieStatus.DROPPED);
    private final Movie OSHI_NO_KO = new Movie(3, "Oshi no ko", MovieStatus.TO_WATCH);

    private final String pathToGetMovieByTitle = "/api/v1/movies/movie/{title}";
    private final String pathToGetMoviesByStatus = "/api/v1/movies/all/{status}";
    private final String pathToGetAllMovies = "/api/v1/movies/all";
    private final String pathToInsert = "/api/v1/movies/insert";
    private final String pathToDelete = "/api/v1/movies/{title}";




    @BeforeEach
    public void init () {
        moviesService = mock(MoviesService.class);
        moviesToDTOMapper = Mappers.getMapper(MoviesToDTOMapper.class);
        moviesController = new MoviesController(moviesService, moviesToDTOMapper);
        objectMapper = new ObjectMapper();
        mvc = MockMvcBuilders.standaloneSetup(moviesController)
                .build();
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
    public void whenBadTitle_thenResponseEntity() {
        doThrow(new MovieNotFoundException()).when(moviesService).getMovieByTitle("Bad");
        assertThrows(MovieNotFoundException.class, () -> moviesController.getMovieByTitle("Bad"));
    }

    @Test
    public void whenDropped_thenMonster() {
        doReturn(List.of(MONSTER)).when(moviesService).getAllMoviesByStatus(MovieStatus.DROPPED);
        assertEquals(MONSTER.getTitle(), moviesController.getMoviesByStatus(MovieStatus.DROPPED).get(0).getTitle());
    }

    @Test
    public void whenDeletePresent_thenOk() {
        doReturn(true).when(moviesService).deleteMovie(anyString());
        assertEquals(HttpStatus.OK, moviesController.deleteMovie(BAKEMONOGATARI.getTitle()).getStatusCode());
    }

    @Test
    public void whenDeleteAbsent_thenThrowMovieNotFound() {
        doThrow(MovieNotFoundException.class).when(moviesService).deleteMovie(anyString());
        assertThrows(MovieNotFoundException.class,
                () -> moviesController.deleteMovie(BAKEMONOGATARI.getTitle()));
    }

    @Test
    public void whenMovieNotFound_thenNotFound() {
        assertEquals(HttpStatus.NOT_FOUND, moviesController.movieNotFound(new MovieNotFoundException()).getStatusCode());
    }

    @Test
    public void whenStatusAbsent_thenBadRequest() {
        assertEquals(HttpStatus.BAD_REQUEST, moviesController.statusNotExist().getStatusCode());
    }


    @Test
    public void whenMovieIsAbsent_thenMessageMovieNotFound () throws Exception {
        doThrow(MovieNotFoundException.class).when(moviesService).getMovieByTitle(any());

        mvc.perform(MockMvcRequestBuilders.get(pathToGetMovieByTitle, "bad")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void whenRequestAll_thenReturnSizeThree() throws Exception {
        doReturn(List.of(BAKEMONOGATARI, MONSTER, OSHI_NO_KO))
                .when(moviesService).getAllMovies();

        mvc.perform(MockMvcRequestBuilders.get(pathToGetAllMovies)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", Matchers.is(BAKEMONOGATARI.getTitle())));
    }

    @Test
    public void whenRequestBakemonogatari_thenBakemonogatari() throws Exception {
        doReturn(BAKEMONOGATARI).when(moviesService).getMovieByTitle(BAKEMONOGATARI.getTitle());
        mvc.perform(MockMvcRequestBuilders.get(pathToGetMovieByTitle, BAKEMONOGATARI.getTitle())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is(BAKEMONOGATARI.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.movieStatus", Matchers.is(BAKEMONOGATARI.getMovieStatus().toString())));
    }

    @Test
    public void whenRequestWarched_thenBakemonogatari() throws Exception {
        doReturn(List.of(BAKEMONOGATARI)).when(moviesService).getAllMoviesByStatus(MovieStatus.WATCHED);
        mvc.perform(MockMvcRequestBuilders.get(pathToGetMoviesByStatus, BAKEMONOGATARI.getMovieStatus()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", Matchers.is(BAKEMONOGATARI.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].movieStatus", Matchers.is(BAKEMONOGATARI.getMovieStatus().toString())));
    }

    @Test
    public void whenRequestWrongStatus_thenBadRequest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(pathToGetMoviesByStatus, "bad"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void whenDeletePresent_ThenOk() throws Exception {
        doReturn(true).when(moviesService).deleteMovie(BAKEMONOGATARI.getTitle());
        mvc.perform(MockMvcRequestBuilders.delete(pathToDelete, BAKEMONOGATARI.getTitle()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void whenDeleteAbsentThenNotFound () throws Exception {
        doThrow(MovieNotFoundException.class).when(moviesService).deleteMovie(any());
        mvc.perform(MockMvcRequestBuilders.delete(pathToDelete, "bad"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void whenInsertAbsent_thenOk() throws Exception {
        doReturn(true).when(moviesService).insertMovie(any());
        String json = objectMapper.writeValueAsString(new MovieDTO("Overlord", MovieStatus.TO_WATCH));
        mvc.perform(MockMvcRequestBuilders.post(pathToInsert).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void whenWrongRequestBody_ThenBadRequest() throws Exception {
        String json = objectMapper.writeValueAsString(new MovieDTO(null, MovieStatus.TO_WATCH));
        mvc.perform(MockMvcRequestBuilders.post(pathToInsert).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void whenInsertPresent_ThenBadRequest() throws Exception {
        doReturn(false).when(moviesService).insertMovie(any());
        String json = objectMapper.writeValueAsString(new MovieDTO(OSHI_NO_KO.getTitle(), MovieStatus.TO_WATCH));
        mvc.perform(MockMvcRequestBuilders.post(pathToInsert).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void whenUpdatePresent_thenOk() throws Exception {
        doReturn(BAKEMONOGATARI).when(moviesService).updateMovie(any(), anyString());
        String json = objectMapper.writeValueAsString(new MovieDTO(BAKEMONOGATARI.getTitle(), BAKEMONOGATARI.getMovieStatus()));
        mvc.perform(MockMvcRequestBuilders.put(pathToGetMovieByTitle, BAKEMONOGATARI.getTitle()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void whenWrongRequestBodyToUpdate_ThenBadRequest() throws Exception {
        String json = objectMapper.writeValueAsString(new MovieDTO(null, MovieStatus.TO_WATCH));
        mvc.perform(MockMvcRequestBuilders.put(pathToGetMovieByTitle, BAKEMONOGATARI.getTitle())
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void whenUpdateAbsent_ThenBadRequest() throws Exception {
        doThrow(MovieNotFoundException.class).when(moviesService).updateMovie(any(), anyString());
        String json = objectMapper.writeValueAsString(new MovieDTO(OSHI_NO_KO.getTitle(), MovieStatus.TO_WATCH));
        mvc.perform(MockMvcRequestBuilders.put(pathToGetMovieByTitle, BAKEMONOGATARI.getTitle())
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
