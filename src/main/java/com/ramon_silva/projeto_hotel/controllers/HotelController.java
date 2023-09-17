package com.ramon_silva.projeto_hotel.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.ramon_silva.projeto_hotel.dto.HotelDto;
import com.ramon_silva.projeto_hotel.dto.HotelImageDto;
import com.ramon_silva.projeto_hotel.dto.PageDto;
import com.ramon_silva.projeto_hotel.services.HotelServiceIMP;
import com.ramon_silva.projeto_hotel.util.HotelImageConstants;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/hotel")
public class HotelController {

  private final HotelServiceIMP hotelIMP;

  public HotelController(HotelServiceIMP hotelIMP) {
    this.hotelIMP = hotelIMP;
  }

  @PostMapping
  @Transactional
  public ResponseEntity<HotelDto> create(@RequestBody @Valid HotelDto hoteldto,
      UriComponentsBuilder uriBuilder) {

    HotelDto hotel = hotelIMP.create(hoteldto);
    var uri = uriBuilder.path("/hotel/{id}").buildAndExpand(hotel.getId()).toUri();
    return ResponseEntity.created(uri).body(hotel);
  }

  @DeleteMapping("/{id}")
  @Transactional
  public ResponseEntity<Void> deleteById(@PathVariable(name = "id") @Positive Long id) {
    hotelIMP.deleteById(id);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/{id}")
  @Transactional
  public ResponseEntity<HotelDto> updateById(@PathVariable(name = "id") Long id,
      @Valid @RequestBody HotelDto hotel) {

    return ResponseEntity.ok().body(hotelIMP.updateById(id, hotel));
  }

  @GetMapping
  public ResponseEntity<PageDto<HotelDto>> getAll(
      @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
      @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
      @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
      @RequestParam(name = "sortOrder", defaultValue = "desc") String sortOrder) {
    return ResponseEntity.ok().body(hotelIMP.getAll(pageNumber, pageSize, sortBy, sortOrder));
  }

  @GetMapping("/{id}")
  public ResponseEntity<HotelDto> getById(@PathVariable(name = "id") @Positive Long id) {
    return new ResponseEntity<HotelDto>(hotelIMP.getById(id), HttpStatus.OK);
  }

  @PostMapping("{id_hotel}/imagens")
  public ResponseEntity<Void> addImage(@PathVariable("id_hotel") Long id,
      @RequestParam(value = HotelImageConstants.BANNER,required=false) MultipartFile banner,
      @RequestParam(value = HotelImageConstants.COMMON_AREA,required=false) MultipartFile commom_area,
      @RequestParam(value = HotelImageConstants.FRONT_DESK,required=false) MultipartFile front_desk,
      @RequestParam(value = HotelImageConstants.OTHER,required=false) MultipartFile other) {
    Map<String, MultipartFile> files = new HashMap();
    files.put(HotelImageConstants.BANNER, banner);
    files.put(HotelImageConstants.COMMON_AREA, commom_area);
    files.put(HotelImageConstants.FRONT_DESK, front_desk);
    files.put(HotelImageConstants.OTHER, other);
    hotelIMP.addImages(id, files);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id_hotel}/imagens/{id_imagem}")
  @Transactional
  public ResponseEntity<Void> deleteImagemById(@PathVariable(name = "id_hotel") @Positive Long id,
      @PathVariable(name = "id_imagem") Long id_imagem) {
    hotelIMP.removeImageById(id, id_imagem);
    return ResponseEntity.ok().build();
  }

  @GetMapping("{id_hotel}/imagens")
  public ResponseEntity<PageDto<HotelImageDto>> getAllImages(
      @PathVariable(name = "id_hotel") Long id,
      @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
      @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
      @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
      @RequestParam(name = "sortOrder", defaultValue = "desc") String sortOrder) {
    return ResponseEntity.ok().body(hotelIMP.getAllImages(id, pageNumber, pageSize, sortBy, sortOrder));
  }
}