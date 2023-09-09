package com.ramon_silva.projeto_hotel.services;
import java.util.List;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ramon_silva.projeto_hotel.dto.GuestDto;
import com.ramon_silva.projeto_hotel.dto.PageDto;
import com.ramon_silva.projeto_hotel.infra.errors.GeralException;
import com.ramon_silva.projeto_hotel.infra.errors.ResourceNotFoundException;
import com.ramon_silva.projeto_hotel.models.GuestModel;
import com.ramon_silva.projeto_hotel.repositories.GuestRepository;
import com.ramon_silva.projeto_hotel.services.interfaces.IGuestService;




@Service
public class GuestServiceIMP implements IGuestService{

    private final GuestRepository guestRepository;
    private final ModelMapper modelMapper;

    public GuestServiceIMP(GuestRepository guestRepository,ModelMapper modelMapper){
      this.guestRepository=guestRepository;
      this.modelMapper=modelMapper;
    }
    
   @Override
    public GuestDto create(GuestDto guestdto) {
        boolean existsDuplicated=guestRepository.existsByEmailAndCpf(guestdto.getEmail(), guestdto.getCpf());
        if(!existsDuplicated){
          GuestModel guestModel=guestRepository.save(modelMapper.map(guestdto,GuestModel.class));
          GuestDto guestDto =modelMapper.map(guestModel,GuestDto.class);
          return guestDto;
        }

        throw new GeralException("Email ou cpf ja cadastrados");
      
      }
     
    

    @Override
    public PageDto<GuestDto> getAll(int pageNumber,int pageSize,String sortBy,String sortOrder){
      Sort sort=sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
      Pageable pageable=PageRequest.of(pageNumber, pageSize, sort);
      Page<GuestModel> page=guestRepository.findAll(pageable);
      List<GuestDto> guestDtos=page.getContent().stream().map((guestModel)->modelMapper.map(guestModel,GuestDto.class)).collect(Collectors.toList());

      PageDto<GuestDto> pageDto=new PageDto<>(guestDtos, page.getNumber(), page.getNumberOfElements(), page.getSize(),
      page.getTotalPages(), page.getTotalElements());
      
      return pageDto;
    }

    @Override
    public GuestDto getById(Long id) {
        GuestModel guestModel=guestRepository.findById(id)
        .orElseThrow(
          ()-> new ResourceNotFoundException("Hospede", "id", id));
        return modelMapper.map(guestModel,GuestDto.class);
    }

    @Override
    public GuestDto updateById(Long id, GuestDto guest) {
    
        guestRepository.findById(id).orElseThrow
        (()->new ResourceNotFoundException("gueste", "id", id));
       
       boolean existsResult = guestRepository.existsByEmailAndCpfAndIdNot(guest.getEmail(),guest.getCpf(), id);
       
       if(existsResult){
        throw new GeralException("Email ou Cpf ja cadastrados");
       }
        guest.setId(id);

        GuestModel guestModel=guestRepository.save(modelMapper.map(guest,GuestModel.class));

        return  modelMapper.map(guestModel, GuestDto.class);
    }

    @Override
    public void deleteById(Long id) {
      GuestModel guest = guestRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("hospede", "id", id));

      guestRepository.deleteById(guest.getId());
     }
    
}
