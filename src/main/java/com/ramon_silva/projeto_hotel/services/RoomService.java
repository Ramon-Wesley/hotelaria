package com.ramon_silva.projeto_hotel.services;

import com.ramon_silva.projeto_hotel.dto.RoomDto;
import com.ramon_silva.projeto_hotel.dto.PageDto;

public interface RoomService {
    
    public RoomDto create(RoomDto room,Long hotel_id);
    public PageDto<RoomDto> getAll(int pageNumber,int pageSize,String sortBy,String sortOrder);
    public RoomDto getById(Long id);
    public RoomDto updateById(Long id,RoomDto room);
    public void deleteById(Long id); 
}
