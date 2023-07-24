package com.ramon_silva.projeto_hotel.services;

import java.util.List;

import com.ramon_silva.projeto_hotel.dto.AddressDto;



public interface AddressService {
    public AddressDto create(AddressDto address);
    public List<AddressDto> getAll();
    public AddressDto getById(Long address);
    public AddressDto updateById(Long id,AddressDto address);
    public AddressDto deleteById(Long id); 
}
