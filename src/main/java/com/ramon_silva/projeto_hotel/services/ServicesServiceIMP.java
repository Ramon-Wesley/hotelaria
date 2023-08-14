package com.ramon_silva.projeto_hotel.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ramon_silva.projeto_hotel.dto.PageDto;
import com.ramon_silva.projeto_hotel.dto.ServicesDto;
import com.ramon_silva.projeto_hotel.infra.errors.ResourceNotFoundException;
import com.ramon_silva.projeto_hotel.models.ServicesModel;
import com.ramon_silva.projeto_hotel.repositories.ServicesRepository;


@Service
public class ServicesServiceIMP implements ServicesService {

    private final ServicesRepository servicesRepository;

    private ServicesServiceIMP(ServicesRepository servicesRepository){
        this.servicesRepository=servicesRepository;
    }

    @Override
    public ServicesDto create(ServicesDto service) {
        ServicesModel servicesModel=servicesRepository.save(new ServicesModel(null,service));

        return new ServicesDto(servicesModel);
    }

    @Override
    public ServicesDto updateById(Long id, ServicesDto service) {
       servicesRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Servicos", "id", id));
                
        ServicesModel servicesModel=new ServicesModel(id,service);
        ServicesModel servicesModel1=servicesRepository.save(servicesModel);
        return new ServicesDto(servicesModel1);
    }

    @Override
    public void deleteById(Long id) {
          servicesRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Servicos", "id", id));
          servicesRepository.deleteById(id);
    }

    @Override
    public ServicesDto getById(Long id) {
       ServicesModel servicesModel=  servicesRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Servicos", "id", id));
          
       return new ServicesDto(servicesModel);
    }

    @Override
    public PageDto<ServicesDto> getAll(int pageNumber, int pageSize, String sortBy, String sortOrder) {
       Sort sort =sortOrder.equalsIgnoreCase("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
       Pageable pageable=PageRequest.of(pageNumber, pageSize, sort);
       Page<ServicesModel> page=servicesRepository.findAll(pageable);
       List<ServicesDto> result=page.getContent().stream().map(ServicesDto::new).collect(Collectors.toList());
       PageDto<ServicesDto> pageDto=new PageDto<>(result,page.getNumber(), page.getNumberOfElements(), page.getSize(),
       page.getTotalPages(), page.getTotalElements()); 
    
       return pageDto;
    }
    
}
