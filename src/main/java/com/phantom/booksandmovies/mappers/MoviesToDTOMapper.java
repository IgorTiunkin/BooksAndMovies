package com.phantom.booksandmovies.mappers;

import com.phantom.booksandmovies.DTO.MovieDTO;
import com.phantom.booksandmovies.models.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MoviesToDTOMapper {

    MovieDTO toMovieDTO (Movie movie);

    @Mapping(target = "id", ignore = true)
    Movie toMovie (MovieDTO movieDTO);
}
