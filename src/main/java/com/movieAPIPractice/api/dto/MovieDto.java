package com.movieAPIPractice.SKMoviebhandar.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter

public class MovieDto {

    private Integer movieId;

    @NotBlank(message = "Please provide movies title")
    private String title;

    @NotBlank(message = "Please provide directors name")
    private String director;

    @NotBlank(message = "Please provide studio name")
    private String studio;

    private Set<String> movieCast;

    private Integer releaseYear;

    @NotBlank(message = "Please provide movies poster")
    private String poster;

    @NotBlank(message = "Provide url")
    private String posterUrl;
}
