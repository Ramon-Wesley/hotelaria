package com.ramon_silva.projeto_hotel.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.ramon_silva.projeto_hotel.dto.HotelImageDto;
import com.ramon_silva.projeto_hotel.dto.PageDto;
import com.ramon_silva.projeto_hotel.dto.RoomDto;
import com.ramon_silva.projeto_hotel.dto.RoomImageDto;
import com.ramon_silva.projeto_hotel.services.RoomServiceIMP;
import com.ramon_silva.projeto_hotel.util.RoomImageConstants;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/quartos")
public class RoomController {

    private RoomServiceIMP roomserviceIMP;

    public RoomController(RoomServiceIMP roomServiceIMP) {
        this.roomserviceIMP = roomServiceIMP;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<RoomDto> create(@RequestBody @Valid RoomDto roomDto,
            UriComponentsBuilder uriComponentsBuilder) {
        var uri = uriComponentsBuilder.path("quartos/{id}").buildAndExpand(roomDto.getId()).toUri();
        return ResponseEntity.created(uri).body(roomserviceIMP.create(roomDto));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteById(@PathVariable(name = "id") @Positive Long id) {
        roomserviceIMP.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<RoomDto> updateById(@PathVariable(name = "id") @Positive Long id,
            @RequestBody @Valid RoomDto roomDto) {

        return ResponseEntity.ok().body(roomserviceIMP.updateById(id, roomDto));
    }

    @GetMapping
    public ResponseEntity<PageDto<RoomDto>> getAll(
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "desc") String sortOrder

    ) {
        return ResponseEntity.ok().body(roomserviceIMP.getAll(pageNumber, pageSize, sortBy, sortOrder));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDto> getById(@PathVariable(name = "id") @Positive Long id) {
        return ResponseEntity.ok().body(roomserviceIMP.getById(id));
    }

    @PostMapping("{id_quarto}/imagens")
    public ResponseEntity<Void> addImage(@PathVariable("id_quarto") Long id,
            @RequestParam(value = RoomImageConstants.SLEPPING_AREA, required = false) MultipartFile sleepingArea,
            @RequestParam(value = RoomImageConstants.BATHROOM, required = false) MultipartFile bathroom,
            @RequestParam(value = RoomImageConstants.OTHER, required = false) MultipartFile other) {
        Map<String, MultipartFile> files = new HashMap();
        files.put(RoomImageConstants.SLEPPING_AREA, sleepingArea);
        files.put(RoomImageConstants.BATHROOM, bathroom);
        files.put(RoomImageConstants.OTHER, other);
        roomserviceIMP.addImages(id, files);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id_quarto}/imagens/{id_imagem}")
    @Transactional
    public ResponseEntity<Void> deleteImagemById(@PathVariable(name = "id_quarto") @Positive Long id,
            @PathVariable(name = "id_imagem") Long id_imagem) {
        roomserviceIMP.removeImageById(id, id_imagem);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{id_quarto}/imagens")
    public ResponseEntity<PageDto<RoomImageDto>> getAllImages(
            @PathVariable(name = "id_quarto") Long id,
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "desc") String sortOrder) {
        return ResponseEntity.ok().body(roomserviceIMP.getAllImages(id, pageNumber, pageSize, sortBy, sortOrder));
    }

}
