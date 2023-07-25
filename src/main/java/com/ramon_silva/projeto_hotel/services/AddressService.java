package com.ramon_silva.projeto_hotel.services;



import com.ramon_silva.projeto_hotel.dto.AddressDto;
import com.ramon_silva.projeto_hotel.dto.PageDto;



public interface AddressService {
    public AddressDto create(AddressDto address);
    public PageDto<AddressDto> getAll(int pageNumber,int pageSize,String sortBy,String sortOrder);
    public AddressDto getById(Long id);
    public AddressDto updateById(Long id,AddressDto address);
    public AddressDto deleteById(Long id); 
}
