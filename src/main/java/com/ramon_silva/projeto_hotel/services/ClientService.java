package com.ramon_silva.projeto_hotel.services;

import java.util.List;

import com.ramon_silva.projeto_hotel.dto.ClientDto;



public interface ClientService {
    public ClientDto create(ClientDto hotel);
    public List<ClientDto> getAll();
    public ClientDto getById(Long hotel);
    public ClientDto updateById(Long id,ClientDto hotel);
    public void deleteById(Long id); 
}
