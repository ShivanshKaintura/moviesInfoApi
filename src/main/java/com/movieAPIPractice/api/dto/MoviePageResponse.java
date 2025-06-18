package com.movieAPIPractice.SKMoviebhandar.dto;

import java.util.List;

public record MoviePageResponse(List<MovieDto> movieDtoList,Integer pageNo,Integer pageSize,
                                long totalElements,
                                int totalPages,
                                boolean isLast) {

}
