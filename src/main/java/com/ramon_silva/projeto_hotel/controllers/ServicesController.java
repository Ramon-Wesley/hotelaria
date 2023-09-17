package com.ramon_silva.projeto_hotel.controllers;

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

import com.ramon_silva.projeto_hotel.dto.PageDto;
import com.ramon_silva.projeto_hotel.dto.ServicesDto;
import com.ramon_silva.projeto_hotel.services.ServicesServiceIMP;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/servicos")
public class ServicesController {

    private final ServicesServiceIMP servicesServiceIMP;

    public ServicesController(ServicesServiceIMP servicesServiceIMP) {
        this.servicesServiceIMP = servicesServiceIMP;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<ServicesDto> create(@RequestBody @Valid ServicesDto service,
            UriComponentsBuilder uriBuilder) {
        ServicesDto servicesDto = servicesServiceIMP.create(service);
        var uri = uriBuilder.path("/services/{id}").buildAndExpand(servicesDto.getId()).toUri();
        return ResponseEntity.created(uri).body(servicesDto);

    }

    @DeleteMapping("{id}")
    @Transactional
    public ResponseEntity<Void> deleteById(@PathVariable(name = "id") @Positive Long id) {
        servicesServiceIMP.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{id}")
    @Transactional
    public ResponseEntity<ServicesDto> updateById(@PathVariable(name = "id") @Positive Long id,
            @RequestBody @Valid ServicesDto roomDto) {

        return ResponseEntity.ok().body(servicesServiceIMP.updateById(id, roomDto));
    }

    @GetMapping
    public ResponseEntity<PageDto<ServicesDto>> getAll(
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "desc") String sortOrder

    ) {
        return ResponseEntity.ok().body(servicesServiceIMP.getAll(pageNumber, pageSize, sortBy, sortOrder));
    }

    @GetMapping("{id}")
    public ResponseEntity<ServicesDto> getById(@PathVariable(name = "id") @Positive Long id) {
        return ResponseEntity.ok().body(servicesServiceIMP.getById(id));
    }
}
