package com.ramon_silva.projeto_hotel.services;



import com.ramon_silva.projeto_hotel.dto.GuestDto;
import com.ramon_silva.projeto_hotel.dto.PageDto;




public interface GuestService {
    public GuestDto create(GuestDto guest) ;
    public PageDto<GuestDto> getAll (int pageNumber,int pageSize,String sortBy,String sortOrder);
    public GuestDto getById(Long id);
    public GuestDto updateById(Long id,GuestDto guest);
    public void deleteById(Long id); 
}
