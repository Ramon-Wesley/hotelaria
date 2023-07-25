package com.ramon_silva.projeto_hotel.dto;

import java.util.List;

public record PageDto<T>(
    List<T>getContent,
    int pageNumber,
    int numberOfElements,
    int pageSize,
    int totalPages,
    long totalElments) {
    

}
