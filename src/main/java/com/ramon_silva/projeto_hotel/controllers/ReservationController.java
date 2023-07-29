package com.ramon_silva.projeto_hotel.controllers;

import org.springframework.http.ResponseEntity;
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
import com.ramon_silva.projeto_hotel.dto.ReservationDto;
import com.ramon_silva.projeto_hotel.services.ReservationServiceIMP;

@RequestMapping("/reserva")
@RestController
public class ReservationController {
    
    private ReservationServiceIMP reservationServiceIMP;
    public ReservationController(ReservationServiceIMP reservationServiceIMP){
        this.reservationServiceIMP=reservationServiceIMP;
    }

    @PostMapping("/{id_cliente}/{id_quarto}")
    public ResponseEntity<ReservationDto> create(@PathVariable(name="id_cliente")Long id_client,
    @PathVariable(name = "id_quarto")Long id_room,@RequestBody ReservationDto reservationDto,
    UriComponentsBuilder uriComponentsBuilder){
        ReservationDto reservationDto2=reservationServiceIMP.createReservation(reservationDto, id_client, id_room);
        var uri=uriComponentsBuilder.path("/reserva/{id_cliente}/{id_quarto}/{id_reserva}").buildAndExpand(id_client,id_room,reservationDto2.id()).toUri();
        return ResponseEntity.created(uri).body(reservationDto2);
    }

     @PutMapping("/{id_reserva}")
    public ResponseEntity<ReservationDto> updateById(@PathVariable(name="id_reserva")Long id_reservation,@RequestBody ReservationDto reservationDto){
        ReservationDto reservationDto2=reservationServiceIMP.confirmReservation(id_reservation);
        return ResponseEntity.ok().body(reservationDto2);
    }
    @GetMapping
    public ResponseEntity<PageDto<ReservationDto>> getAllgetAll(
 
        @RequestParam(name = "pageNumber",defaultValue = "0")int pageNumber,
        @RequestParam(name = "pageSize",defaultValue = "10")int pageSize,
        @RequestParam(name = "sortBy",defaultValue = "id")String sortBy,
        @RequestParam(name = "sortOrder",defaultValue = "desc")String sortOrder
      ){
        return ResponseEntity.ok().body(reservationServiceIMP.getAllReservation(pageNumber, pageSize, sortBy, sortOrder));
      }
    
}