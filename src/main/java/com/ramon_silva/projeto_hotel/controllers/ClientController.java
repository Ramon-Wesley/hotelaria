package com.ramon_silva.projeto_hotel.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.ramon_silva.projeto_hotel.dto.ClientDto;
import com.ramon_silva.projeto_hotel.dto.PageDto;
import com.ramon_silva.projeto_hotel.services.ClientServiceIMP;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/clientes")
public class ClientController {
    
    @Autowired
    private ClientServiceIMP clientServiceIMP;


    @PostMapping
    @Transactional
    public ResponseEntity<ClientDto> create(@RequestBody @Valid ClientDto client,UriComponentsBuilder uriBuilder){
        ClientDto clientDto=clientServiceIMP.create(client);
        var uri=uriBuilder.path("/clientes/{id}").buildAndExpand(clientDto.id()).toUri();
        return ResponseEntity.created(uri).body(clientDto);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ClientDto> updateById(@PathVariable(name="id")Long id,@RequestBody @Valid ClientDto client){
        return ResponseEntity.ok().body(clientServiceIMP.updateById(id, client));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteById(@PathVariable(name="id") Long id){
        clientServiceIMP.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getById(@PathVariable(name="id") Long id){
        return ResponseEntity.ok().body(clientServiceIMP.getById(id));
    }

    @GetMapping
    public ResponseEntity<PageDto<ClientDto>> getAll(
        @RequestParam(name = "pageNumber",defaultValue = "0")int pageNumber,
        @RequestParam(name = "pageSize",defaultValue = "10")int pageSize,
        @RequestParam(name = "sortBy",defaultValue = "id")String sortBy,
        @RequestParam(name = "sortOrder",defaultValue = "desc")String sortOrder

    ){
        return ResponseEntity.ok().body(clientServiceIMP.getAll(pageNumber, pageSize, sortBy, sortOrder));
    }
}
