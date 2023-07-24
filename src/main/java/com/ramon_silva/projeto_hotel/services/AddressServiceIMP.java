package com.ramon_silva.projeto_hotel.services;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ramon_silva.projeto_hotel.dto.AddressDto;
import com.ramon_silva.projeto_hotel.infra.errors.ResourceNotFoundException;
import com.ramon_silva.projeto_hotel.models.AddressModel;
import com.ramon_silva.projeto_hotel.repositories.AddressRepository;

@Service
public class AddressServiceIMP implements AddressService{

    @Autowired
    AddressRepository addressRepository;

    @Override
    public AddressDto create(AddressDto address) {
        AddressModel addressRes=addressRepository.save(new AddressModel(address));
        return new AddressDto(addressRes);
    }

    @Override
    public List<AddressDto> getAll() {
      
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

    @Override
    public AddressDto getById(Long address) {
        AddressModel addressModel=addressRepository.findById(address).orElseThrow(() ->
        new ResourceNotFoundException("Endereço", "id", address) );
    return new AddressDto(addressModel);
    }

    @Override
    public AddressDto updateById(Long id, AddressDto address) {
      
        throw new UnsupportedOperationException("Unimplemented method 'updateById'");
    }

    @Override
    public AddressDto deleteById(Long id) {
       
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    
}
