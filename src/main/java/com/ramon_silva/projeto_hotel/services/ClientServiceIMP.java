package com.ramon_silva.projeto_hotel.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ramon_silva.projeto_hotel.dto.ClientDto;
import com.ramon_silva.projeto_hotel.infra.errors.ResourceNotFoundException;
import com.ramon_silva.projeto_hotel.models.ClientModel;
import com.ramon_silva.projeto_hotel.repositories.ClientRepository;


@Service
public class ClientServiceIMP implements ClientService{

    @Autowired
    private ClientRepository clientRepository;

    
    @Override
    public ClientDto create(ClientDto client) {
      ClientModel clientModel=clientRepository.save(new ClientModel(client));
      return new ClientDto(clientModel);
    }

    @Override
    public List<ClientDto> getAll() {
        List<ClientDto> client=clientRepository.findAll()
        .stream()
        .map(ClientDto::new)
        .collect(Collectors.toList());
        return client;
    }

    @Override
    public ClientDto getById(Long client) {
        ClientModel clientmodel=clientRepository.findById(client).orElseThrow(()-> new ResourceNotFoundException("Cliente", "id", client));

        return new ClientDto(clientmodel);
    }

    @Override
    public ClientDto updateById(Long id, ClientDto client) {
        clientRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("cliente", "id", id));

        ClientModel clientModel=clientRepository.save(new ClientModel(client));

        return new ClientDto(clientModel);
    }

    @Override
    public void deleteById(Long id) {
        clientRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("cliente", "id", id));
    }
    
}
