package com.ramon_silva.projeto_hotel.services;

import java.util.List;
import java.util.stream.Collectors;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ramon_silva.projeto_hotel.dto.HotelDto;
import com.ramon_silva.projeto_hotel.dto.PageDto;
import com.ramon_silva.projeto_hotel.infra.errors.GeralException;
import com.ramon_silva.projeto_hotel.infra.errors.ResourceNotFoundException;
import com.ramon_silva.projeto_hotel.models.HotelModel;
import com.ramon_silva.projeto_hotel.repositories.HotelRepository;

@Service
public class HotelServiceIMP implements HotelService {

    
    private final HotelRepository hotelRepository;


    private HotelServiceIMP(HotelRepository hotelRepository){

        this.hotelRepository=hotelRepository;
    }

    @Override
    public HotelDto create(HotelDto hotel) {
        boolean existsCnpj=hotelRepository.existsByCnpj(hotel.cnpj());

        if(!existsCnpj){
            HotelModel hotelModel=hotelRepository.save(new HotelModel(null,hotel));
            return new HotelDto(hotelModel);
        }
        throw new GeralException("Cnpj já cadastrado!");
    }

    @Override
    public PageDto<HotelDto> getAll(int pageNumber,int pageSize,String sortBy,String sortOrder){
     Sort sort=sortOrder.equalsIgnoreCase("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
     Pageable pageable=PageRequest.of(pageNumber, pageSize, sort);
     Page<HotelModel> page=hotelRepository.findAll(pageable);
     List<HotelDto> hotelDtos=page.getContent().stream().map(HotelDto::new).collect(Collectors.toList());
     PageDto<HotelDto> result = new PageDto<>(hotelDtos, page.getNumber(), page.getNumberOfElements(), page.getSize(),
     page.getTotalPages(), page.getTotalElements());
     return result;
     }

    @Override
    public HotelDto getById(Long id) {
     HotelModel hotelModel=hotelRepository.findById(id)
     .orElseThrow(()-> new ResourceNotFoundException("Hotel", "id", id));

     return new HotelDto(hotelModel);
    }

    @Override
    public HotelDto updateById(Long id, HotelDto hotel) {
    
     hotelRepository.findById(id).orElseThrow(
        ()-> new ResourceNotFoundException("Hotel", "id", id));

        boolean existsCnpj=hotelRepository.existsByCnpjAndIdNot(hotel.cnpj(),id);
        if(!existsCnpj){
            HotelModel result= hotelRepository.save(new HotelModel(id,hotel));
            return new HotelDto(result);
        }
        throw new GeralException("Cnpj já cadastrado!");
    }

    @Override
    public void deleteById(Long id) {
       hotelRepository.findById(id).orElseThrow(
        ()-> new ResourceNotFoundException("Hotel", "id", id)
        );
       hotelRepository.deleteById(id);
    }


}
