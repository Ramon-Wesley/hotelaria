package com.ramon_silva.projeto_hotel.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ramon_silva.projeto_hotel.dto.PageDto;
import com.ramon_silva.projeto_hotel.dto.RoomDto;
import com.ramon_silva.projeto_hotel.infra.errors.ResourceNotFoundException;
import com.ramon_silva.projeto_hotel.models.HotelModel;
import com.ramon_silva.projeto_hotel.models.RoomModel;
import com.ramon_silva.projeto_hotel.repositories.HotelRepository;
import com.ramon_silva.projeto_hotel.repositories.RoomRepository;


@Service
public class RoomServiceIMP implements RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;

    private RoomServiceIMP(RoomRepository roomRepository,HotelRepository hotelRepository,ModelMapper modelMapper){
        this.roomRepository=roomRepository;
        this.hotelRepository=hotelRepository;
        this.modelMapper=modelMapper;
    }
    
    @Override
    public RoomDto create(RoomDto room) {
        HotelModel hotel=hotelRepository.findById(room.getHotel().getId())
        .orElseThrow(()-> 
        new ResourceNotFoundException("Hotel", "id", room.getHotel().getId()));

        RoomModel roomModel=modelMapper.map(room,RoomModel.class);
        roomModel.setHotel(hotel);
        RoomModel result=roomRepository.save(roomModel);

        return modelMapper.map(result,RoomDto.class);
    }

    @Override
    public PageDto<RoomDto> getAll(String hotel,int pageNumber, int pageSize, String sortBy, String sortOrder) {
        Sort sort =sortOrder.equalsIgnoreCase("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
        Pageable pageable=PageRequest.of(pageNumber, pageSize, sort);
        Page<RoomModel> page=roomRepository.findAll(pageable);
        List<RoomDto> roomDtos=page.getContent().stream().map((result)->modelMapper.map(result,RoomDto.class)).collect(Collectors.toList());
        PageDto<RoomDto> pageDto=new PageDto<>(roomDtos,page.getNumber(), page.getNumberOfElements(), page.getSize(),
        page.getTotalPages(), page.getTotalElements());
        return pageDto;
    }

    @Override
    public RoomDto getById(Long id) {
       RoomModel roomModel=roomRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Quartos", "id", id));

       return modelMapper.map(roomModel,RoomDto.class);
    }

    @Override
    public RoomDto updateById(Long id, RoomDto room) {
        roomRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Quartos", "id", id));

        RoomModel roomModel=roomRepository.save(modelMapper.map(room,RoomModel.class));

        return modelMapper.map(roomModel,RoomDto.class);
    }

    @Override
    public void deleteById(Long id) {
        roomRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Quartos", "id", id));
        roomRepository.deleteById(id);
    }
    
}
