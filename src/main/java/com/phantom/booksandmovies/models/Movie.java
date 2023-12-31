package com.phantom.booksandmovies.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table (name = "movie")
public class Movie implements Serializable {

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
