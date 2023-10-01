package com.phantom.booksandmovies.DTO;

import com.phantom.booksandmovies.models.MovieStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Information about movie")
public class MovieDTO {

    @ApiModelProperty(value = "Title of movie")
    @NotEmpty (message = "title should not be empty")
    @Size(max = 255, message = "size should not be more than 255 characters")
    private String title;

    @ApiModelProperty(value = "Status of movie")
    private MovieStatus movieStatus;
}
