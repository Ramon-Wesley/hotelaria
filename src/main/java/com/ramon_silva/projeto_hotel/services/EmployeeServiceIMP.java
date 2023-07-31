package com.ramon_silva.projeto_hotel.services;

import java.util.List;
import java.util.stream.Collectors;

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

@Service
public class EmployeeServiceIMP implements EmployeeService {

    
    private final EmployeeRepository employeeRepository;
    private EmployeeServiceIMP(EmployeeRepository employeeRepository){
        this.employeeRepository=employeeRepository;
    }

    @Override
    public EmployeeDto create(EmployeeDto employee) {
        EmployeeModel employeeModel=employeeRepository.save(new EmployeeModel(null,employee));
        return new EmployeeDto(employeeModel);
    }

    @Override
     public PageDto<EmployeeDto> getAll(int pageNumber,int pageSize,String sortBy,String sortOrder){
       Sort sort =sortOrder.equalsIgnoreCase("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
       Pageable pageable=PageRequest.of(pageNumber, pageSize, sort);
       Page<EmployeeModel> page=employeeRepository.findAll(pageable);
       List<EmployeeDto> employeeDtos= page.getContent().stream().map(EmployeeDto::new).collect(Collectors.toList());
       PageDto<EmployeeDto> pageDto=new PageDto<>(employeeDtos,page.getNumber(), page.getNumberOfElements(), page.getSize(),
       page.getTotalPages(), page.getTotalElements());
       return pageDto;
    }

    @Override
    public EmployeeDto getById(Long id) {
        EmployeeModel employeemodel=employeeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Empregados", "id", id));
        return new EmployeeDto(employeemodel);
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
