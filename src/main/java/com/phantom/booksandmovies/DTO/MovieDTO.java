package com.phantom.booksandmovies.DTO;

import com.phantom.booksandmovies.models.MovieStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {

    @NotEmpty (message = "title should not be empty")
    private String title;

    private MovieStatus movieStatus;
}
