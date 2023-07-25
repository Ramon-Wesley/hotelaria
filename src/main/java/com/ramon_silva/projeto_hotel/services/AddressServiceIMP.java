package com.ramon_silva.projeto_hotel.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ramon_silva.projeto_hotel.dto.AddressDto;
import com.ramon_silva.projeto_hotel.dto.PageDto;
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
    public PageDto<AddressDto> getAll(int pageNumber,int pageSize,String sortBy,String sortOrder) {
      Sort sort=sortOrder.equalsIgnoreCase("desc")? Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
      Pageable pageable= PageRequest.of(pageNumber, pageSize, sort);
      Page<AddressModel> page=addressRepository.findAll(pageable);
      List<AddressDto> addressDtos=page.getContent().stream().map(AddressDto::new).collect(Collectors.toList());

      PageDto<AddressDto> pageDto=new PageDto<>(addressDtos, page.getNumber(), page.getNumberOfElements(), page.getSize(),
       page.getTotalPages(), page.getTotalElements());

       return pageDto;
     
    }

    @Override
    public AddressDto getById(Long id) {
        AddressModel addressModel=addressRepository.findById(id).orElseThrow(() ->
        new ResourceNotFoundException("Endereço", "id", id) );
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
