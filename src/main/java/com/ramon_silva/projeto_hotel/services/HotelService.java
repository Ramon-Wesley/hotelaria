
package com.ramon_silva.projeto_hotel.services;

import com.ramon_silva.projeto_hotel.dto.HotelDto;
import com.ramon_silva.projeto_hotel.dto.PageDto;

public interface HotelService {
    public HotelDto create(HotelDto hotel);
    public PageDto<HotelDto> getAll(int pageNumber,int pageSize,String sortBy,String sortOrder);
    public HotelDto getById(Long hotel);
    public HotelDto updateById(Long id,HotelDto hotel);
    public void deleteById(Long id); 
}