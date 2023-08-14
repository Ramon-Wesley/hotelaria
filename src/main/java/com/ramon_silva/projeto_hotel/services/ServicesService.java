package com.ramon_silva.projeto_hotel.services;

import com.ramon_silva.projeto_hotel.dto.PageDto;
import com.ramon_silva.projeto_hotel.dto.ServicesDto;

public interface ServicesService {
    
    public ServicesDto create(ServicesDto service);
    public ServicesDto updateById(Long id, ServicesDto service);
    public void deleteById(Long id);
    public ServicesDto getById(Long id);
    public PageDto<ServicesDto> getAll(int pageNumber,int pageSize,String sortBy,String sortOrder);
}
