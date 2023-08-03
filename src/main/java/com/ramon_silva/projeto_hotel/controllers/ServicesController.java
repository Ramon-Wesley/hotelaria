package com.ramon_silva.projeto_hotel.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.ramon_silva.projeto_hotel.dto.ServicesDto;
import com.ramon_silva.projeto_hotel.services.ServicesServiceIMP;

@RestController
@RequestMapping("/servicos")
public class ServicesController {
    
    private final ServicesServiceIMP servicesServiceIMP;

    public ServicesController(ServicesServiceIMP servicesServiceIMP){
        this.servicesServiceIMP=servicesServiceIMP;
    }

    @PostMapping
    public ResponseEntity<ServicesDto> create(@RequestBody ServicesDto service,UriComponentsBuilder uriBuilder){
        ServicesDto servicesDto=servicesServiceIMP.create(service);
        var uri=uriBuilder.path("/services/{id}").buildAndExpand(servicesDto.id()).toUri();
        return ResponseEntity.created(uri).body(servicesDto);
        
    }
}
