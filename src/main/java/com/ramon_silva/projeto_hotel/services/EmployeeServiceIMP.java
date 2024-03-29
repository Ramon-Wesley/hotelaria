package com.ramon_silva.projeto_hotel.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ramon_silva.projeto_hotel.dto.EmployeeDto;
import com.ramon_silva.projeto_hotel.dto.PageDto;
import com.ramon_silva.projeto_hotel.infra.errors.ResourceNotFoundException;
import com.ramon_silva.projeto_hotel.models.EmployeeModel;
import com.ramon_silva.projeto_hotel.repositories.EmployeeRepository;
import com.ramon_silva.projeto_hotel.services.interfaces.IEmployeeService;

@Service
public class EmployeeServiceIMP implements IEmployeeService {

    
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;
    private EmployeeServiceIMP(EmployeeRepository employeeRepository,ModelMapper modelMapper){
        this.employeeRepository=employeeRepository;
        this.modelMapper=modelMapper;
    }

    @Override
    public EmployeeDto create(EmployeeDto employee) {
        EmployeeModel employeeModel=employeeRepository.save(modelMapper.map(employee,EmployeeModel.class));
        return modelMapper.map(employeeModel,EmployeeDto.class);
    }

    @Override
     public PageDto<EmployeeDto> getAll(int pageNumber,int pageSize,String sortBy,String sortOrder){
       Sort sort =sortOrder.equalsIgnoreCase("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
       Pageable pageable=PageRequest.of(pageNumber, pageSize, sort);
       Page<EmployeeModel> page=employeeRepository.findAll(pageable);
       List<EmployeeDto> employeeDtos= page.getContent().stream().map((e)->modelMapper.map(e,EmployeeDto.class)).collect(Collectors.toList());
       PageDto<EmployeeDto> pageDto=new PageDto<>(employeeDtos,page.getNumber(), page.getNumberOfElements(), page.getSize(),
       page.getTotalPages(), page.getTotalElements());
       return pageDto;
    }

    @Override
    public EmployeeDto getById(Long id) {
        EmployeeModel employeemodel=employeeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Empregados", "id", id));
        return modelMapper.map(employeemodel,EmployeeDto.class);
    }

    @Override
    public EmployeeDto updateById(Long id, EmployeeDto employee) {

        throw new UnsupportedOperationException("Unimplemented method 'updateById'");
    }

    @Override
    public void deleteById(Long id) {
     
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }
    
}
