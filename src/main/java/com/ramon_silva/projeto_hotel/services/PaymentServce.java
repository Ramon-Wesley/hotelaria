package com.ramon_silva.projeto_hotel.services;

import com.ramon_silva.projeto_hotel.dto.ClientDto;
import com.ramon_silva.projeto_hotel.dto.PageDto;
import com.ramon_silva.projeto_hotel.dto.PaymentDto;

public interface PaymentServce {
    public PaymentDto payment(PaymentDto paymentDto);
    public PaymentDto getPaymentById(Long id);
    public PageDto<PaymentDto> getAll(int pageNumber,int pageSize,String sortBy,String sortOrder);
}
