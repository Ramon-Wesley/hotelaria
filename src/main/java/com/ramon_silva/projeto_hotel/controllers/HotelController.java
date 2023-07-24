package com.ramon_silva.projeto_hotel.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ramon_silva.projeto_hotel.dto.HotelDto;
import com.ramon_silva.projeto_hotel.services.HotelServiceIMP;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/hotel")
public class HotelController {
    

    @Autowired
    private HotelServiceIMP hotelIMP;

    @PostMapping
    @Transactional
    public ResponseEntity<HotelDto> create(@RequestBody @Valid HotelDto hoteldto){
     return new ResponseEntity<HotelDto>(hotelIMP.create(hoteldto),HttpStatus.CREATED); 
    }

    @DeleteMapping("/{id}")
    @Transactional
     public ResponseEntity<?> deleteById(@PathVariable(name="id") Long id){
    hotelIMP.deleteById(id);
     return ResponseEntity.ok().body("Deletado com sucesso!"); 
    }

    @PutMapping("/{id}")
    @Transactional
     public ResponseEntity<HotelDto> updateById(@PathVariable(name="id") Long id,@Valid @RequestBody HotelDto hotel){
    
     return ResponseEntity.ok().body(hotelIMP.updateById(id, hotel)); 
    }

    @GetMapping
      public ResponseEntity<List<HotelDto>> getAll(){
      return new ResponseEntity<List<HotelDto>>(hotelIMP.getAll(),HttpStatus.OK); 
    }

    @GetMapping("/{id}") 
     public ResponseEntity<HotelDto> getById(@PathVariable(name = "id") Long id){
      return new ResponseEntity<HotelDto>(hotelIMP.getById(id),HttpStatus.OK); 
    }
}
