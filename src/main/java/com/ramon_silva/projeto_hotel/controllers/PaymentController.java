package com.ramon_silva.projeto_hotel.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.ramon_silva.projeto_hotel.dto.PaymentDto;
import com.ramon_silva.projeto_hotel.services.PaymentServiceIMP;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/pagamento")
public class PaymentController {
    
    private final PaymentServiceIMP paymentServiceIMP;
    public PaymentController(PaymentServiceIMP paymentServiceIMP){
        this.paymentServiceIMP=paymentServiceIMP;
    }

    @PostMapping("/{reservation_id}")
    public ResponseEntity<PaymentDto> create(@PathVariable(name = "reservation_id")Long id,@RequestBody @Valid PaymentDto paymentDto,UriComponentsBuilder uriComponentsBuilder){

        PaymentDto paymentDto2=paymentServiceIMP.payment(paymentDto, id);
        var uri=uriComponentsBuilder.path("/pagamento/{reservation_id}/{payment_id}").buildAndExpand(id,paymentDto2.id()).toUri();
        return ResponseEntity.created(uri).body(paymentDto2);
    }
}
