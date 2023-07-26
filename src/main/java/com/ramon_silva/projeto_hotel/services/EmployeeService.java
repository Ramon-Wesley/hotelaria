package com.ramon_silva.projeto_hotel.services;



import com.ramon_silva.projeto_hotel.dto.EmployeeDto;
import com.ramon_silva.projeto_hotel.dto.PageDto;

public interface EmployeeService {
     public EmployeeDto create(EmployeeDto employee);
    public PageDto<EmployeeDto> getAll(int pageNumber,int pageSize,String sortBy,String sortOrder);
    public EmployeeDto getById(Long id);
    public EmployeeDto updateById(Long id,EmployeeDto employee);
    public void deleteById(Long id); 
}
