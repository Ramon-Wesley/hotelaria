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

import com.ramon_silva.projeto_hotel.dto.GuestDto;
import com.ramon_silva.projeto_hotel.dto.PageDto;
import com.ramon_silva.projeto_hotel.services.GuestServiceIMP;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/hospedes")
public class GuestController {
    
    private final GuestServiceIMP guestServiceIMP;
    public GuestController(GuestServiceIMP guestServiceIMP){
        this.guestServiceIMP=guestServiceIMP;
    }


    @PostMapping
    @Transactional
    public ResponseEntity<GuestDto> create(@RequestBody @Valid GuestDto guest,UriComponentsBuilder uriBuilder){
        GuestDto guestDto=guestServiceIMP.create(guest);
        var uri=uriBuilder.path("/guestes/{id}").buildAndExpand(guestDto.getId()).toUri();
        return ResponseEntity.created(uri).body(guestDto);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<GuestDto> updateById(@PathVariable(name="id")@Positive Long id, @RequestBody @Valid GuestDto guest){
        
        return ResponseEntity.ok().body(guestServiceIMP.updateById(id, guest));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteById(@PathVariable(name="id") @Positive Long id){
        guestServiceIMP.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuestDto> getById(@PathVariable(name="id") @Positive Long id){
        return ResponseEntity.ok().body(guestServiceIMP.getById(id));
    }

    @GetMapping
    public ResponseEntity<PageDto<GuestDto>> getAll(
        @RequestParam(name = "pageNumber",defaultValue = "0")int pageNumber,
        @RequestParam(name = "pageSize",defaultValue = "10")int pageSize,
        @RequestParam(name = "sortBy",defaultValue = "id")String sortBy,
        @RequestParam(name = "sortOrder",defaultValue = "desc")String sortOrder

    ){
        return ResponseEntity.ok().body(guestServiceIMP.getAll(pageNumber, pageSize, sortBy, sortOrder));
    }
}
