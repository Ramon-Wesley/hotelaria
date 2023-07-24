package com.ramon_silva.projeto_hotel.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ramon_silva.projeto_hotel.dto.AddressDto;
import com.ramon_silva.projeto_hotel.dto.HotelDto;
import com.ramon_silva.projeto_hotel.infra.errors.ResourceNotFoundException;
import com.ramon_silva.projeto_hotel.models.AddressModel;
import com.ramon_silva.projeto_hotel.models.HotelModel;
import com.ramon_silva.projeto_hotel.repositories.HotelRepository;

@Service
public class HotelServiceIMP implements HotelService {

    @Autowired
    HotelRepository hotelRepository;

    @Autowired 
    AddressServiceIMP addressServiceIMP;

    @Override
    public HotelDto create(HotelDto hotel) {
        HotelModel hotelModel=hotelRepository.save(new HotelModel(hotel));
        
        return new HotelDto(hotelModel);
    }

    @Override
    public List<HotelDto> getAll() {
     List<HotelModel> hotel=hotelRepository.findAll();
     return hotel.stream().map(HotelDto::new).collect(Collectors.toList());
    }

    @Override
    public HotelDto getById(Long id) {
     HotelModel hotelModel=hotelRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Hotel", "id", id));

     return new HotelDto(hotelModel);
    }

    @Override
    public HotelDto updateById(Long id, HotelDto hotel) {
     HotelModel hotelModel=hotelRepository.findById(id).orElseThrow(
        ()-> new ResourceNotFoundException("Hotel", "id", id));
        hotelRepository.save(new HotelModel(hotel));
        hotelModel.setName(hotel.name());
        hotelModel.setDescription(hotel.description());
        hotelModel.setClassification(hotel.classification());
    
        // Atualiza o endereço do hotel
        AddressDto addressDto = hotel.address();
        if (addressDto != null) {
            AddressModel addressModel = hotelModel.getAddress();
            if (addressModel == null) {
                addressModel = new AddressModel(addressDto);
            } else {
                addressModel.setCountry(addressDto.country());
                addressModel.setState(addressDto.state());
                addressModel.setZapCode(addressDto.zapCode());
                addressModel.setCity(addressDto.city());
                addressModel.setNeighborhood(addressDto.neighborhood());
                addressModel.setNumber(addressDto.number());
                addressModel.setComplemement(addressDto.complemement());
            }
            hotelModel.setAddress(addressModel);
        }
 
        hotelRepository.save(hotelModel);
    
        
        return new HotelDto(hotelModel);
    }

    @Override
    public void deleteById(Long id) {
       hotelRepository.findById(id).orElseThrow(
        ()-> new ResourceNotFoundException("Hotel", "id", id)
        );
       hotelRepository.deleteById(id);
    }

    
}
