package com.ramon_silva.projeto_hotel.services.interfaces;

import com.ramon_silva.projeto_hotel.dto.PageDto;

public interface IGenericInterface<T> {
    public T create(T address);

    public PageDto<T> getAll(int pageNumber, int pageSize, String sortBy, String sortOrder);

    public T getById(Long id);

    public T updateById(Long id, T address);

    public void deleteById(Long id);
}
