package com.movieAPIPractice.api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer movieId;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "Please provide movies title")
    private String title;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "Please provide directors name")
    private String director;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "Please provide studio name")
    private String studio;

    @ElementCollection
    @CollectionTable(name = "movie_cast")
    private Set<String> movieCast;

    @Column(nullable = false, length = 255)

    private Integer releaseYear;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "Please provide movies poster")
    private String poster;
}
