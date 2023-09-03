package com.ramon_silva.projeto_hotel.dto;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class PageDto<T> implements Serializable{
    private Collection<? extends T> getContent;
    private int pageNumber;
    private int numberOfElements;
    private int pageSize;
    private int totalPages;
    private long totalElments;
    

}
