package com.ramon_silva.projeto_hotel.dto;

import java.util.Collection;

public record PageDto<T>(
    Collection<? extends T> getContent,
    int pageNumber,
    int numberOfElements,
    int pageSize,
    int totalPages,
    long totalElments) {
    

}
