package com.phantom.booksandmovies.DTO;

import com.phantom.booksandmovies.models.MovieStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {

    private String title;

    private MovieStatus movieStatus;
}
