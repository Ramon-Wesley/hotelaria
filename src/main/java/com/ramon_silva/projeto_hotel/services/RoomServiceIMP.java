package com.ramon_silva.projeto_hotel.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.ramon_silva.projeto_hotel.dto.EmployeeDto;
import com.ramon_silva.projeto_hotel.dto.PageDto;
import com.ramon_silva.projeto_hotel.dto.RoomDto;
import com.ramon_silva.projeto_hotel.infra.errors.ResourceNotFoundException;
import com.ramon_silva.projeto_hotel.models.RoomModel;
import com.ramon_silva.projeto_hotel.repositories.RoomRepository;

public class RoomServiceIMP implements RoomService {

    private final RoomRepository roomRepository;

    public RoomServiceIMP(RoomRepository roomRepository){
        this.roomRepository=roomRepository;
    }
    
    @Override
    public RoomDto create(RoomDto room) {
        RoomModel roomModel=roomRepository.save(new RoomModel(room));

        return new RoomDto(roomModel);
    }

    @Override
    public PageDto<RoomDto> getAll(int pageNumber, int pageSize, String sortBy, String sortOrder) {
        Sort sort =sortOrder.equalsIgnoreCase("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
        Pageable pageable=PageRequest.of(pageNumber, pageSize, sort);
        Page<RoomModel> page=roomRepository.findAll(pageable);
        List<RoomDto> roomDtos=page.getContent().stream().map(RoomDto::new).collect(Collectors.toList());
        PageDto<RoomDto> pageDto=new PageDto<>(roomDtos,page.getNumber(), page.getNumberOfElements(), page.getSize(),
        page.getTotalPages(), page.getTotalElements());
        return pageDto;
    }

    @Override
    public RoomDto getById(Long id) {
       RoomModel roomModel=roomRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Quartos", "id", id));

       return new RoomDto(roomModel);
    }

    @Override
    public RoomDto updateById(Long id, RoomDto room) {
        roomRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Quartos", "id", id));

        RoomModel roomModel=roomRepository.save(new RoomModel(room));

        return new RoomDto(roomModel);
    }

    @Override
    public void deleteById(Long id) {
        roomRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Quartos", "id", id));
        roomRepository.deleteById(id);
    }
    
}
