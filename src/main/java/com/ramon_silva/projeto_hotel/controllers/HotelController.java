package com.ramon_silva.projeto_hotel.controllers;


import org.springframework.http.HttpStatus;
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

import com.ramon_silva.projeto_hotel.dto.HotelDto;
import com.ramon_silva.projeto_hotel.dto.PageDto;
import com.ramon_silva.projeto_hotel.services.HotelServiceIMP;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/hotel")
public class HotelController {

    private final HotelServiceIMP hotelIMP;

  public HotelController(HotelServiceIMP hotelIMP)
  {
     this.hotelIMP=hotelIMP;
   }
    @PostMapping
    @Transactional
    public ResponseEntity<HotelDto> create(@RequestBody @Valid HotelDto hoteldto,UriComponentsBuilder uriBuilder){
      HotelDto hotel=hotelIMP.create(hoteldto);
      var uri=uriBuilder.path("/hotel/{id}").buildAndExpand(hotel.id()).toUri();
     return  ResponseEntity.created(uri).body(hotel); 
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
      public ResponseEntity<PageDto<HotelDto>> getAll(
        @RequestParam(name = "pageNumber",defaultValue = "0")int pageNumber,
        @RequestParam(name = "pageSize",defaultValue = "10")int pageSize,
        @RequestParam(name = "sortBy",defaultValue = "id")String sortBy,
        @RequestParam(name = "sortOrder",defaultValue = "desc")String sortOrder
      ){
      return ResponseEntity.ok().body(hotelIMP.getAll(pageNumber, pageSize, sortBy, sortOrder)); 
    }

    @GetMapping("/{id}") 
     public ResponseEntity<HotelDto> getById(@PathVariable(name = "id") Long id){
      return new ResponseEntity<HotelDto>(hotelIMP.getById(id),HttpStatus.OK); 
    }
}