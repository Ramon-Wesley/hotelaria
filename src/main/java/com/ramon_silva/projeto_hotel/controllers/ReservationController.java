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
import com.ramon_silva.projeto_hotel.dto.ReservationDatesDto;
import com.ramon_silva.projeto_hotel.dto.ReservationDto;
import com.ramon_silva.projeto_hotel.dto.Reservation_serviceDto;

import com.ramon_silva.projeto_hotel.services.ReservationServiceIMP;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;


@RequestMapping("/reserva")
@RestController
public class ReservationController {
    
    private ReservationServiceIMP reservationServiceIMP;
    public ReservationController(ReservationServiceIMP reservationServiceIMP){
        this.reservationServiceIMP=reservationServiceIMP;
    }

    @PostMapping("/{id_cliente}/{id_quarto}")
    public ResponseEntity<ReservationDto> create(@PathVariable(name="id_cliente")Long id_client,
    @PathVariable(name = "id_quarto")Long id_room,@Valid @RequestBody ReservationDatesDto reservationDatesDto,
    UriComponentsBuilder uriComponentsBuilder){
        ReservationDto reservationDto2=reservationServiceIMP.createReservation(reservationDatesDto, id_client, id_room);
        var uri=uriComponentsBuilder.path("/reserva/{id_cliente}/{id_quarto}/{id_reserva}").buildAndExpand(id_client,id_room,reservationDto2.id()).toUri();
        return ResponseEntity.created(uri).body(reservationDto2);
    }

    @PostMapping("/{id_reserva}/servicos/{id_servico}")
    public ResponseEntity<Void> addServices(@PathVariable(name = "id_reserva")Long id,
    @PathVariable(name = "id_servico") @Positive Long service_id){
        reservationServiceIMP.addServices(id, service_id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id_reserva}/servicos/{id_servico}")
     public ResponseEntity<Void> removeServices(@PathVariable(name = "id_reserva")Long id,
       @PathVariable(name = "id_servico")Long service_id){
        reservationServiceIMP.removeServices(id, service_id);
        return ResponseEntity.ok().build();
    }

     @PutMapping("/{id_reserva}")
    public ResponseEntity<ReservationDto> updateById(@PathVariable(name="id_reserva")Long id_reservation,@RequestBody @Valid ReservationDatesDto reservationDatesDto){
        ReservationDto reservationDto2=reservationServiceIMP.confirmReservation(id_reservation);
        return ResponseEntity.ok().body(reservationDto2);
    }

        @GetMapping("/{id_reserva}")
    public ResponseEntity<ReservationDto> getById(@PathVariable(name="id_reserva")Long id_reservation){
        ReservationDto reservationDto2=reservationServiceIMP.getReservationById(id_reservation);
        return ResponseEntity.ok().body(reservationDto2);
    }
    @GetMapping
    public ResponseEntity<PageDto<ReservationDto>> getAll(
 
        @RequestParam(name = "pageNumber",defaultValue = "0")int pageNumber,
        @RequestParam(name = "pageSize",defaultValue = "10")int pageSize,
        @RequestParam(name = "sortBy",defaultValue = "id")String sortBy,
        @RequestParam(name = "sortOrder",defaultValue = "desc")String sortOrder
      ){
        return ResponseEntity.ok().body(reservationServiceIMP.getAllReservation(pageNumber, pageSize, sortBy, sortOrder));
      }
    @GetMapping("/{id_reserva}/servicos")
    public ResponseEntity<PageDto<Reservation_serviceDto>> getAllReservation_service(
        @PathVariable(name="id_reserva") Long id,
        @RequestParam(name = "pageNumber",defaultValue = "0")int pageNumber,
        @RequestParam(name = "pageSize",defaultValue = "10")int pageSize,
        @RequestParam(name = "sortBy",defaultValue = "id")String sortBy,
        @RequestParam(name = "sortOrder",defaultValue = "desc")String sortOrder
      ){
        return ResponseEntity.ok().body(reservationServiceIMP.getAllServicesReservation(id, pageNumber, pageSize, sortBy, sortOrder));
      }

        @GetMapping("/servicos")
        public ResponseEntity<PageDto<Reservation_serviceDto>> getAllService_reservations(
        @RequestParam(name = "pageNumber",defaultValue = "0")int pageNumber,
        @RequestParam(name = "pageSize",defaultValue = "10")int pageSize,
        @RequestParam(name = "sortBy",defaultValue = "id")String sortBy,
        @RequestParam(name = "sortOrder",defaultValue = "desc")String sortOrder
      ){
        return ResponseEntity.ok().body(reservationServiceIMP.getAll(pageNumber, pageSize, sortBy, sortOrder));
     }
    
}
