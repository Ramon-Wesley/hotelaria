package com.ramon_silva.projeto_hotel.services;

import java.util.List;

import com.ramon_silva.projeto_hotel.dto.HotelDto;

public interface HotelService {
    public HotelDto create(HotelDto hotel);
    public List<HotelDto> getAll();
    public HotelDto getById(Long hotel);
    public HotelDto updateById(Long id,HotelDto hotel);
    public void deleteById(Long id); 
}
