package com.ramon_silva.projeto_hotel.services;



import com.ramon_silva.projeto_hotel.dto.ClientDto;
import com.ramon_silva.projeto_hotel.dto.PageDto;



public interface ClientService {
    public ClientDto create(ClientDto client);
    public PageDto<ClientDto> getAll (int pageNumber,int pageSize,String sortBy,String sortOrder);
    public ClientDto getById(Long id);
    public ClientDto updateById(Long id,ClientDto client);
    public void deleteById(Long id); 
}
