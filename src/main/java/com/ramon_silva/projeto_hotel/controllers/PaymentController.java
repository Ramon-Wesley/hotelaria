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
import com.ramon_silva.projeto_hotel.enums.PaymentMethodEnum;
import com.ramon_silva.projeto_hotel.services.PaymentServiceIMP;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/pagamento")
public class PaymentController {

    private final PaymentServiceIMP paymentServiceIMP;

    public PaymentController(PaymentServiceIMP paymentServiceIMP) {
        this.paymentServiceIMP = paymentServiceIMP;
    }

    @PostMapping("/reserva/{reservation_id}")
    @Transactional
    public ResponseEntity<PaymentDto> payment(
            @PathVariable(name = "reservation_id") @Positive Long id,
            @RequestBody PaymentMethodEnum paymentDto,
            UriComponentsBuilder uriComponentsBuilder) {
        PaymentDto paymentDto2 = paymentServiceIMP.create(id,paymentDto);
        var uri = uriComponentsBuilder.path("/pagamento/{payment_id}").buildAndExpand(paymentDto2.getId()).toUri();
        return ResponseEntity.created(uri).body(paymentDto2);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteById(@PathVariable(name = "id") @Positive Long id) {

        paymentServiceIMP.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<PaymentDto> updateById(@PathVariable(name = "id") @Positive Long id,
            @RequestBody @Valid PaymentDto payment) {
        PaymentDto paymentDto = paymentServiceIMP.updateById(id, payment);
        return ResponseEntity.ok().body(paymentDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> getById(
            @PathVariable(name = "id") @Positive Long id) {
        PaymentDto paymentDto = paymentServiceIMP.getById(id);
        return ResponseEntity.ok().body(paymentDto);
    }

    @GetMapping
    public ResponseEntity<PageDto<PaymentDto>> getAll(
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "desc") String sortOrder

    ) {
        return ResponseEntity.ok().body(paymentServiceIMP.getAll(pageNumber, pageSize, sortBy, sortOrder));
    }
}
