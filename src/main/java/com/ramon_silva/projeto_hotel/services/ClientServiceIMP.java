package com.ramon_silva.projeto_hotel.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ramon_silva.projeto_hotel.dto.ClientDto;
import com.ramon_silva.projeto_hotel.dto.PageDto;
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
    public PageDto<ClientDto> getAll(int pageNumber,int pageSize,String sortBy,String sortOrder){
      Sort sort=sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
      Pageable pageable=PageRequest.of(pageNumber, pageSize, sort);
      Page<ClientModel> page=clientRepository.findAll(pageable);
      List<ClientDto> clientDtos=page.getContent().stream().map(ClientDto::new).collect(Collectors.toList());

      PageDto<ClientDto> pageDto=new PageDto<>(clientDtos, page.getNumber(), page.getNumberOfElements(), page.getSize(),
      page.getTotalPages(), page.getTotalElements());
      return pageDto;
    }

    @Override
    public ClientDto getById(Long id) {
        ClientModel clientmodel=clientRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Cliente", "id", id));

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
