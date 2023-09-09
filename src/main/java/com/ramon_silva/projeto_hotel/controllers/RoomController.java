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
import com.ramon_silva.projeto_hotel.dto.RoomDto;
import com.ramon_silva.projeto_hotel.services.RoomServiceIMP;

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

}
