package com.phantom.booksandmovies.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "movie")
public class Movie {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private MovieStatus movieStatus;

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", movieStatus=" + movieStatus +
                '}';
    }
}
