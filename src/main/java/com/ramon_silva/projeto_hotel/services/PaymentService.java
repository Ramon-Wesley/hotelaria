package com.ramon_silva.projeto_hotel.services;


import com.ramon_silva.projeto_hotel.dto.PageDto;
import com.ramon_silva.projeto_hotel.dto.PaymentDto;

public interface PaymentService {
    public PaymentDto payment(PaymentDto paymentDto,Long reservation_id);
    public PaymentDto getPaymentById(Long id);
    public void deletePaymentById(Long id);
    public PaymentDto updateById(Long id,PaymentDto paymentDto);
   public PageDto<PaymentDto> getAll(int pageNumber,int pageSize,String sortBy,String sortOrder);
   
    

}
