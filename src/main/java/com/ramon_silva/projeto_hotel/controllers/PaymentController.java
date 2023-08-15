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
    public ResponseEntity<PaymentDto> payment(
        @PathVariable(name = "reservation_id")Long id,
        @RequestBody @Valid PaymentDto paymentDto,
        UriComponentsBuilder uriComponentsBuilder){
        PaymentDto paymentDto2=paymentServiceIMP.payment(paymentDto, id);
        var uri=uriComponentsBuilder.path("/pagamento/{reservation_id}/{payment_id}").buildAndExpand(id,paymentDto2.id()).toUri();
        return ResponseEntity.created(uri).body(paymentDto2);
    }

    @DeleteMapping("/{id}")
     public ResponseEntity<Void> deleteById(@PathVariable(name = "reservation_id")Long id){
       
       paymentServiceIMP.deletePaymentById(id);
        return ResponseEntity.ok().build();
    }
     @PutMapping("/{id}")
        public ResponseEntity<PaymentDto> updateById(@PathVariable(name = "id")Long id,
        @RequestBody @Valid PaymentDto payment){
       PaymentDto paymentDto=paymentServiceIMP.updateById(id,payment);
        return ResponseEntity.ok().body(paymentDto);
    }
     @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> getById(
        @PathVariable(name="id") Long id){
            PaymentDto paymentDto=paymentServiceIMP.getPaymentById(id);
            return ResponseEntity.ok().body(paymentDto);
    }

    @GetMapping
    public ResponseEntity<PageDto<PaymentDto>> getAll(
    @RequestParam(name = "pageNumber",defaultValue = "0")int pageNumber,
    @RequestParam(name = "pageSize",defaultValue = "10")int pageSize,
    @RequestParam(name = "sortBy",defaultValue = "id")String sortBy,
    @RequestParam(name = "sortOrder",defaultValue = "desc")String sortOrder

    ){
        return ResponseEntity.ok().body(paymentServiceIMP.getAll(pageNumber, pageSize, sortBy, sortOrder));
    }
}
